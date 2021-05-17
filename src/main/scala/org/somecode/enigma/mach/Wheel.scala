package org.somecode.enigma
package mach

final case class Wheel private (forward: Vector[Position], ringSetting: Position, notches: Set[Position]) extends Machine.Bus:
  val reverse: Vector[Position] = forward.zipWithIndex.sortBy(_._1.value).map((n, off) => Position.unsafe(off))
 
  override def lookup(state: Machine.State, in: Position): Position = forward((in + ringSetting).value)
  override def reverseLookup(state: Machine.State, in: Position): Position = reverse((in - ringSetting).value)

  def next(): (Machine.State, Position) = ???

object Wheel:
  // def apply(letterMap: String, ringSetting: Position): Either[String, Wheel] =
  //   parseLetterMap(letterMap).map(vec => new Wheel(vec, ringSetting))
  

  /** Create Wheel from a letter map */
  def apply(s: String, ringSetting: Position, notches: Set[Position]): Either[String, Wheel] = s.toUpperCase match
    case s if s.length != Position.Max => Left(s"Letter maps must contain exactly $Position.Max characters.")
    case s if s.length != s.distinct.length => Left(s"Letter maps must not contain duplicate characters.")
    case s if s.exists(c => c < 'A' || c > 'Z') => Left(s"Letter maps must contain only letters from 'A' to 'Z', inclusive.")
    case s => apply(s.toVector.map(c => Position.unsafe(c - 'A')), ringSetting, notches)
    //val forward: Vector[Position] = offsets.map(Position.unsafe)

  /** Create Wiring from a vector of offsets  */
  def apply(v: Vector[Position], ringSetting: Position, notches: Set[Position]): Either[String, Wheel] = v match
    case v if v.length != Position.Max => Left(s"Wiring vectors must contain exactly ${Position.Max} values.")
    case v if v.length != v.distinct.length => Left(s"Wiring vectors must not contain duplicate values.")
    case v if v.exists(p => p.value < 0 || p.value > Position.Max) => Left(s"Wiring vectors must contain only values from 0 to ${Position.Max}, inclusive.")
    case v => Right(new Wheel(v, ringSetting, notches))


  // private def parseLetterMap(s: String): Either[String, Vector[Int]] = s.toUpperCase match
  //   case s if s.length != Position.Max => Left(s"Letter maps must contain exactly ${Position.Max} characters.")
  //   case s if s.length != s.distinct.length => Left(s"Letter maps must not contain duplicate characters.")
  //   case s if s.exists(c => c < 'A' || c > 'Z') => Left(s"Letter maps must contain only letters from 'A' to 'Z', inclusive.")
  //   case s => Right(s.toVector.map(_ - 'A'))

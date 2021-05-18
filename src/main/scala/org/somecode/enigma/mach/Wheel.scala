package org.somecode.enigma
package mach

class Wheel private[mach] (forward: Vector[Position], val ringSetting: Position, val notches: Set[Position]) extends Machine.Bus:
  val reverse: Vector[Position] = forward.zipWithIndex.sortBy(_._1.value).map((n, off) => Position.unsafe(off))
 
  override def lookup(state: Machine.State, in: Position): Position = forward((in + ringSetting).value)
  override def reverseLookup(state: Machine.State, in: Position): Position = reverse((in - ringSetting).value)

  def shouldAdvance(p: Position): Boolean = notches.contains(p)

  private def copy(
    forward: Vector[Position] = forward,
    ringSetting: Position = ringSetting,
    notches: Set[Position] = notches): Either[String, Wheel] =
      Wheel.apply(forward, ringSetting, notches)

object Wheel:
  // def apply(letterMap: String, ringSetting: Position): Either[String, Wheel] =
  //   parseLetterMap(letterMap).map(vec => new Wheel(vec, ringSetting))

  def apply(s: String, ringSetting: Position, notches: Set[Position]): Either[String, Wheel] =
    validateLetterMap(s).map(vv => new Wheel(vv, ringSetting, notches))

  def apply(v: Vector[Position], ringSetting: Position, notches: Set[Position]): Either[String, Wheel] =
    validatePositionVector(v).map(vv => new Wheel(vv, ringSetting, notches))

  // s.toUpperCase match
  //   case s if s.length != Position.Max => Left(s"Letter maps must contain exactly $Position.Max characters.")
  //   case s if s.length != s.distinct.length => Left(s"Letter maps must not contain duplicate characters.")
  //   case s if s.exists(c => c < 'A' || c > 'Z') => Left(s"Letter maps must contain only letters from 'A' to 'Z', inclusive.")
  //   case s => apply(s.toVector.map(c => Position.unsafe(c - 'A')), ringSetting, notches)

  /** Create Wiring from a vector of offsets  */
  // def apply(v: Vector[Position], ringSetting: Position, notches: Set[Position]): Either[String, Wheel] = v match
  //   case v if v.length != Position.Max => Left(s"Wiring vectors must contain exactly ${Position.Max} values.")
  //   case v if v.length != v.distinct.length => Left(s"Wiring vectors must not contain duplicate values.")
  //   case v if v.exists(p => p.value < 0 || p.value > Position.Max) => Left(s"Wiring vectors must contain only values from 0 to ${Position.Max}, inclusive.")
  //   case v => Right(new Wheel(v, ringSetting, notches))

  /** Create Wheel from a letter map */
  def validateLetterMap(s: String): Either[String, Vector[Position]] = s.toUpperCase match
    case s if s.length != Position.Max => Left(s"Letter maps must contain exactly $Position.Max characters.")
    case s if s.length != s.distinct.length => Left(s"Letter maps must not contain duplicate characters.")
    case s if s.exists(c => c < 'A' || c > 'Z') => Left(s"Letter maps must contain only letters from 'A' to 'Z', inclusive.")
    case s => Right(s.toVector.map(c => Position.unsafe(c - 'A')))

  /** Create Wiring from a vector of offsets  */
  def validatePositionVector(v: Vector[Position]): Either[String, Vector[Position]] = v match
    case v if v.length != Position.Max => Left(s"Wiring vectors must contain exactly ${Position.Max} values.")
    case v if v.length != v.distinct.length => Left(s"Wiring vectors must not contain duplicate values.")
    case v if v.exists(p => p.value < 0 || p.value > Position.Max) => Left(s"Wiring vectors must contain only values from 0 to ${Position.Max}, inclusive.")
    case v => Right(v)


  // private def parseLetterMap(s: String): Either[String, Vector[Int]] = s.toUpperCase match
  //   case s if s.length != Position.Max => Left(s"Letter maps must contain exactly ${Position.Max} characters.")
  //   case s if s.length != s.distinct.length => Left(s"Letter maps must not contain duplicate characters.")
  //   case s if s.exists(c => c < 'A' || c > 'Z') => Left(s"Letter maps must contain only letters from 'A' to 'Z', inclusive.")
  //   case s => Right(s.toVector.map(_ - 'A'))

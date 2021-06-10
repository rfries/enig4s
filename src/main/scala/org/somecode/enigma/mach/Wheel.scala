package org.somecode.enigma
package mach

case class Wheel(
  wiring: Wiring,
  ringSetting: Position,
  notches: Set[Position]) extends Machine.Bus:

  override def lookup(state: Machine.State, in: Position): Position =
    wiring.forward(in + ringSetting)

  override def reverseLookup(state: Machine.State, in: Position): Position =
    wiring.reverse(in - ringSetting)

  def shouldAdvance(p: Position): Boolean = notches.contains(p)

object Wheel:
  // def apply(letterMap: String, ringSetting: Position): Either[String, Wheel] =
  //   parseLetterMap(letterMap).map(vec => new Wheel(vec, ringSetting))

  def apply(s: String, ringSetting: Position, notches: Set[Position]): Either[String, Wheel] =
    Wiring.fromString(s).map(Wheel(_, ringSetting, notches))
    //validateLetterMap(s).map(vv => new Wheel(vv, ringSetting, notches))

  def apply(v: Vector[Position], ringSetting: Position, notches: Set[Position]): Either[String, Wheel] =
    Wiring.fromPositions(v).map(Wheel(_, ringSetting, notches))
    //validatePositionVector(v).map(vv => new Wheel(vv, ringSetting, notches))

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
  // def validateLetterMap(s: String): Either[String, Vector[Position]] = s.toUpperCase match
  //   case s if s.length != Position.Max => Left(s"Letter maps must contain exactly $Position.Max characters.")
  //   case s if s.length != s.distinct.length => Left(s"Letter maps must not contain duplicate characters.")
  //   case s if s.exists(c => c < 'A' || c > 'Z') => Left(s"Letter maps must contain only letters from 'A' to 'Z', inclusive.")
  //   case s => Right(s.toVector.map(c => Position.unsafe(c - 'A')))

  // /** Create Wiring from a vector of offsets  */
  // def validatePositionVector(v: Vector[Position]): Either[String, Vector[Position]] = v match
  //   case v if v.length != Position.Max => Left(s"Wiring vectors must contain exactly ${Position.Max} values.")
  //   case v if v.length != v.distinct.length => Left(s"Wiring vectors must not contain duplicate values.")
  //   case v if v.exists(p => p.value < 0 || p.value > Position.Max) => Left(s"Wiring vectors must contain only values from 0 to ${Position.Max}, inclusive.")
  //   case v => Right(v)


  // private def parseLetterMap(s: String): Either[String, Vector[Int]] = s.toUpperCase match
  //   case s if s.length != Position.Max => Left(s"Letter maps must contain exactly ${Position.Max} characters.")
  //   case s if s.length != s.distinct.length => Left(s"Letter maps must not contain duplicate characters.")
  //   case s if s.exists(c => c < 'A' || c > 'Z') => Left(s"Letter maps must contain only letters from 'A' to 'Z', inclusive.")
  //   case s => Right(s.toVector.map(_ - 'A'))

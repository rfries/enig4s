package org.somecode.enigma
package mach

sealed abstract case class Wiring private (forward: Vector[KeyCode]):
  val reverse: Vector[KeyCode] = forward
    .zipWithIndex
    .sortBy(_._1.toInt)
    .map((n, off) => KeyCode.unsafe(off))

object Wiring:
  def fromString(letterMap: String): Either[String, Wiring] = letterMap.toUpperCase match
    case s if s.length != KeyCode.Max => Left(s"Letter maps must contain exactly ${KeyCode.Max} characters.")
    case s if s.length != s.distinct.length => Left(s"Letter maps must not contain duplicate characters.")
    case s if s.exists(c => c < 'A' || c > 'Z') => Left(s"Letter maps must contain only letters from 'A' to 'Z', inclusive.")
    case s => Right(new Wiring(s.toVector.map( c => KeyCode.unsafe(c - 'A'))) {} )

  def fromOffsets(offsets: Vector[Int]): Either[String, Wiring] = offsets match
    case v if v.length != KeyCode.Max => Left(s"Wiring vectors must contain exactly ${KeyCode.Max} values.")
    case v if v.length != v.distinct.length => Left(s"Wiring vectors must not contain duplicate values.")
    case v if v.exists(n => n < 0 || n >= KeyCode.Max) => Left(s"Wiring vectors must contain only values from 0 to ${KeyCode.Max}, inclusive.")
    case v => Right(new Wiring(v.map(KeyCode.unsafe(_))) {} )

  /** Create Wiring from a vector of offsets  */
  def fromPositions(positions: Vector[KeyCode]): Either[String, Wiring] = positions match
    case v if v.length != KeyCode.Max => Left(s"Wiring vectors must contain exactly ${KeyCode.Max} values.")
    case v if v.length != v.distinct.length => Left(s"Wiring vectors must not contain duplicate values.")
    case v => Right(new Wiring(v) {})

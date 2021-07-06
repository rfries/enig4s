package org.somecode.enigma
package mach

sealed abstract case class Wiring private (forward: Vector[KeyCode]):
  val reverse: Vector[KeyCode] = forward
    .zipWithIndex
    .sortBy(_._1.toInt)
    .map((n, idx) => KeyCode.unsafe(idx))

  def size: Int = forward.size

object Wiring:
  def fromString(letterMap: String): Either[String, Wiring] = letterMap.toUpperCase match
    case s if s.isEmpty => Left(s"Letter maps must not be zero length.")
    case s if s.length != s.distinct.length => Left(s"Letter maps must not contain duplicate characters.")
    case s if s.exists(c => c < 'A' || c > 'Z') => Left(s"Letter maps must contain only letters from 'A' to 'Z'.")
    case s => Right(new Wiring(s.toVector.map( c => KeyCode.unsafe(c - 'A'))) {} )

  /** Create Wiring from a vector of offsets  */
  def fromVector(keyCodes: Vector[KeyCode]): Either[String, Wiring] = keyCodes match
    case v if v.length <= 0 => Left("Wiring vectors must contain more than 0 entries.")
    case v if v.length != v.distinct.length => Left(s"Wiring vectors must not contain duplicate values.")
    case v if v.exists(n => n < 0 || n >= v.length) => Left(s"Wiring vectors must contain only values from 0 to ${v.length}, inclusive.")
    case v => Right(new Wiring(v) {})


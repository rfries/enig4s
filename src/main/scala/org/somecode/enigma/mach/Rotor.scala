package org.somecode.enigma
package mach

final case class Rotor(xlate: Vector[Char])

object Rotor:
  def fromString(xlateString: String): Either[String, Rotor] =
    xlateString.toUpperCase match
      case s if s.length != 26 => Left("Translate string must be 26 characters.")
      case s if s.distinct.length != 26 => Left("Translate string must contain no duplicate letters.")
      case s => Right(Rotor(s.toVector))
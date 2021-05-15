package org.somecode.enigma
package mach

final case class Wheel private (translate: Vector[Int], ringSetting: Position)

object Wheel:
  def apply(letterMap: String, ringSetting: Position): Either[String, Wheel] =
    parseLetterMap(letterMap).map(vec => new Wheel(vec, ringSetting))

  private def parseLetterMap(s: String): Either[String, Vector[Int]] = s.toUpperCase match
    case s if s.length != Position.Max => Left(s"Letter maps must contain exactly ${Position.Max} characters.")
    case s if s.length != s.distinct.length => Left(s"Letter maps must not contain duplicate characters.")
    case s if s.exists(c => c < 'A' || c > 'Z') => Left(s"Letter maps must contain only letters from 'A' to 'Z', inclusive.")
    case s => Right(s.toVector.map(_ - 'A'))

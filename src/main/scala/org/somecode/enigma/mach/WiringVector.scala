package org.somecode.enigma
package mach

final case class WiringVector private (vector: Vector[Int])

object WiringVector:
  val Lines: Int = Position.Max

  /** Create a WiringVector from a letter map */
  def apply(s: String): Either[String, WiringVector] = s.toUpperCase match
    case s if s.length != Lines => Left(s"Letter maps must contain exactly $Lines characters.")
    case s if s.length != s.distinct.length => Left(s"Letter maps must not contain duplicate characters.")
    case s if s.exists(c => c < 'A' || c > 'Z') => Left(s"Letter maps must contain only letters from 'A' to 'Z', inclusive.")
    case s => Right(new WiringVector(s.toVector.map(_ - 'A')))

  /** Create a WiringVector from a vector of Ints  */
  def apply(v: Vector[Int]): Either[String, WiringVector] = v match
    case v if v.length != Lines => Left(s"Wiring vectors must contain exactly $Lines values.")
    case v if v.length != v.distinct.length => Left(s"Wiring vectors must not contain duplicate values.")
    case v if v.exists(n => n < 0 || n > Lines) => Left(s"Wiring vectors must contain only values from 0 to $Lines, inclusive.")
    case v => Right(new WiringVector(v))

  //val direct: WiringVector = new WiringVector((0 to Lines-1).toVector)

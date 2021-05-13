package org.somecode.enigma
package mach

import cats.*

trait Wiring (offsets: Vector[Int]):
  val reversed = offsets.zipWithIndex.sortBy(_._1).map(_._2)
 
  //def lookup(pos: Value, v: Value): Value
  //def reverseLookup(pos: Value, v: Value): Value

object Wiring:
  val Paths = 26
  /** Create Wiring from a letter map */
  def apply(s: String): Either[String, Wiring] = s.toUpperCase match
    case s if s.length != Paths => Left(s"Letter maps must contain exactly $Paths characters.")
    case s if s.length != s.distinct.length => Left(s"Letter maps must not contain duplicate characters.")
    case s if s.exists(c => c < 'A' || c > 'Z') => Left(s"Letter maps must contain only letters from 'A' to 'Z', inclusive.")
    case s => apply(s.toVector.map(_ - 'A'))

  /** Create Wiring from a vector of offsets  */
  def apply(v: Vector[Int]): Either[String, Wiring] = v match
    case v if v.length != Paths => Left(s"Wiring vectors must contain exactly $Paths values.")
    case v if v.length != v.distinct.length => Left(s"Wiring vectors must not contain duplicate values.")
    case v if v.exists(n => n < 0 || n > Paths) => Left(s"Wiring vectors must contain only values from 0 to $Paths, inclusive.")
    case v => Right(new Wiring(v) {} )

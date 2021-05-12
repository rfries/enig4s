package org.somecode.enigma
package mach

import cats._
import cats.implicits._
import cats.data._

case class Rotor private (
  name: Rotor.Name,
  wiring: Wiring,
  notches: Set[Rotor.Position],
  ringSetting: Rotor.Position):

  import Rotor._

  def lookup(in: Int, pos: Position): Either[String, (Boolean, Position)] =
    ???

object Rotor:

  val RotorSize = 26

  opaque type Name = String
  object Name:

    def apply(s: String): Either[String, Name] = s.trim match
      case s if s.isEmpty => Left("Name cannot be blank.")
      case s => Right(s)
  
  end Name

  opaque type Position = Int
  object Position:

    def apply(c: Int): Either[String, Position] =
      if (c < 0 || c >= RotorSize)
        Left(s"Position must be between 0 and $RotorSize (inclusive)")
      else
        Right(c)

    def unsafe(c: Int): Position = apply(c) match
      case Left(s) => throw new IllegalArgumentException(s)
      case Right(p) => p

    // given Show[Position] with
    //   def show(p: Position): String = p.value.toInt.formatted("%02x")

  end Position

  extension (p: Position)
    def next: Int = ((p + 1) % RotorSize)
    def value: Int = p

  def apply(name: String, wiring: String, notches: Set[Int], ringSetting: Int): Either[String, Rotor] =
    validate(name, wiring, notches, ringSetting)

  private def validate(
    name: String,
    wiring: String,
    notches: Set[Int],
    ringSetting: Int): Either[String, Rotor] =
      for
        vname   <- Name(name)
        vwire   <- Wiring(wiring)
        vnpos   <- notches.toVector.traverse(Position.apply)
        vnotch  <- validateNotches(vnpos.toSet)
        vring   <- Position(ringSetting)
      yield
        new Rotor(vname, vwire, vnotch, vring)

  private def validateNotches(notches: Set[Position]): Either[String, Set[Position]] =
    notches match
      case v if v.size > RotorSize => Left("Notches must not be larger than rotor size.")
      case v => Right(v)

end Rotor

/*
  case class Wiring private (v: Vector[Int]):
    val reversed: Map[Int,Int] = v.zipWithIndex.toMap

      // reversed translation lookup for return path through the rotor
    val translate: Map[Int,Int] =
      reversed.map(tup => tup._2 -> tup._1).toMap

  object Wiring:

    def apply(xlate: String): Either[String, Wiring] = xlate.toUpperCase match
      case s if s.length != RotorSize => Left(s"Wiring string must be $RotorSize characters.")
      case s if s.distinct.length != RotorSize => Left("Wiring string must contain no duplicate letters.")
      case s if s.exists(c => c < 'A' || c > 'Z') => Left("Wiring string must contain only letters between 'A' and 'Z'.")
      case s => Right(new Wiring(s.toVector.map(_ - 'A')))


    def apply(xlate: Vector[Int]): Either[String, Wiring] = xlate match
      case v if v.length != RotorSize => Left(s"Wiring vector must be $RotorSize elements.")
      case v if v.distinct.length != RotorSize => Left("Wiring string must contain no duplicate letters.")
      case v if v.exists(c => c < 0 || c > RotorSize) => Left(s"Wiring string must contain values between 0 and $RotorSize.")
      case v => Right(new Wiring(v))

  end Wiring
*/

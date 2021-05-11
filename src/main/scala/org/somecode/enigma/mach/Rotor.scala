package org.somecode.enigma
package mach

import cats._
import cats.implicits._
import cats.data._

case class Rotor private (
  name: Rotor.Name,
  wiring: Rotor.Wiring,
  notches: Vector[Rotor.Position],
  ringSetting: Rotor.Position,
  offset: Char):

  import Rotor._

  // reversed translation lookup for return path through the rotor
  val reversed: Map[Char,Char] =
    wiring.v.zipWithIndex.map(tup => tup._2.toChar -> tup._1).toMap

  def lookup(in: Char, pos: Position): Either[String, Int] = ???

object Rotor:

  val RotorSize = 26

  opaque type Name = String
  object Name:

    def apply(s: String): Either[String, Name] = s.trim match
      case s if s.isEmpty => Left("Name cannot be blank.")
      case s => Right(s)
  
  end Name

  opaque type Position = Char
  object Position:

    def apply(c: Char): Either[String, Position] =
      if (c < 0 || c >= RotorSize)
        Left(s"Position must be between 0 and $RotorSize (inclusive)")
      else
        Right(c)

    def unsafe(c: Char): Position = apply(c) match
      case Left(s) => throw new IllegalArgumentException(s)
      case Right(p) => p

    given Show[Position] with
      def show(p: Position): String = p.value.toInt.formatted("%02x")

  end Position

  extension (p: Position)
    def next: Char = (p % RotorSize).toChar
    def value: Char = p
    //def toString: String = (p :Char).toString
    //def show: String = s"<${p.value.toString}>"
    def show: String = show"<$p>"

  case class Wiring private (v: Vector[Char])

  object Wiring:

    def apply(s: String): Either[String, Wiring] =
      apply(s.toUpperCase.toVector)

    def apply(xlate: Vector[Char]): Either[String, Wiring] = xlate match
      case v if v.length != RotorSize => Left("Wiring string must be 26 characters.")
      case v if v.distinct.length != RotorSize => Left("Wiring string must contain no duplicate letters.")
      case v if v.exists(c => c < 'A' || c > 'Z') => Left("Wiring string must contain only letters.")
      case v => Right(new Wiring(v))

  end Wiring

  def apply(name: String, wiring: String, notches: Vector[Char], ringSetting: Char): Either[String, Rotor] =
    validate(name, wiring, notches, ringSetting)

  private def validate(
    name: String,
    wiring: String,
    notches: Vector[Char],
    ringSetting: Char): Either[String, Rotor] =
      for
        vname   <- Name(name)
        vwire   <- Wiring(wiring)
        vnpos   <- notches.traverse(Position.apply)
        vnotch  <- validateNotches(vnpos)
        vring   <- Position(ringSetting)
      yield
        new Rotor(vname, vwire, vnotch, vring, 0)

  private def validateNotches(notches: Vector[Position]): Either[String, Vector[Position]] =
    notches match
      case v if v.length > RotorSize => Left("Notches must not be larger than rotor size.")
      case v if v.length != v.distinct.length => Left("Notches must not contain duplicates.")
      case v => Right(v)

end Rotor

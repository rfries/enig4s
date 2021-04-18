package org.somecode.enigma
package mach

import cats._
import cats.implicits._
import cats.data._

case class Rotor private (
  name: Rotor.Name,
  wiring: Rotor.Wiring,
  notches: Vector[Rotor.Position],
  ringSetting: Rotor.Position):

  import Rotor._

  val reversed: Map[Int,Int] =
    wiring.v.zipWithIndex.map(tup => tup._2 -> tup._1.toInt).toMap

  def lookup(in: Int, pos: Int): Either[String, Int] = ???

object Rotor:
  val RotorSize = 26

  case class Position private (n: Int):
    def copy(n: Int = n): Either[String, Position] = Position(n)

  object Position:
    def apply(n: Int): Either[String, Position] =
     if n < 0 || n >= RotorSize then Left("Position must be between 0 and 25 (inclusive)")
     else Right(new Position(n))

  case class Wiring private (v: Vector[Char])

  object Wiring:
    def apply(s: String): Either[String, Wiring] = apply(s.toUpperCase.toVector)

    def apply(v: Vector[Char]): Either[String, Wiring] = v match
      case v if v.length != RotorSize => Left("Wiring string must be 26 characters.")
      case v if v.distinct.length != RotorSize => Left("Wiring string must contain no duplicate letters.")
      case v if v.exists(c => c < 'A' || c > 'Z') => Left("Wiring string must contain only letters.")
      case v => Right(new Wiring(v))

  case class Name private (s: String)

  object Name:
    def apply(s: String): Either[String, Name] = s.trim match
      case s if s.isEmpty => Left("Name cannot be blank.")
      case s => Right(new Name(s))

  private def apply(name: String, wiring: String, notches: Vector[Int], ringSetting: Int): Either[String, Rotor] =
    validate(name, wiring, notches, ringSetting)

  private def validateNotches(notches: Vector[Position]): Either[String, Vector[Position]] = notches match
    case v if v.length > RotorSize => Left("Notches must not be larger than rotor size.")
    case v if v.length != v.distinct.length => Left("Notches must not contain duplicates.")
    case v => Right(v)

  private def validate(
    name: String,
    wiring: String,
    notches: Vector[Int],
    ringSetting: Int): Either[String, Rotor] =
      for
        vname <- Name(name)
        vwiring <- Wiring(wiring)
        vn <- notches.traverse(Position.apply)
        vnotches <- validateNotches(vn)
        vringSetting <- Position(ringSetting)
      yield new Rotor(vname, vwiring, vnotches, vringSetting)
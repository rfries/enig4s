package org.somecode.enigma
package mach

import cats._
import cats.implicits._
import cats.data._

case class Rotor private (
  name: String,
  wiring: Vector[Char],
  notches: Vector[Rotor.Position],
  ringSetting: Rotor.Position):

  import Rotor._

  val reversed: Map[Int,Int] =
    wiring.zipWithIndex.map(tup => tup._2 -> tup._1.toInt).toMap

  def lookup(in: Int, pos: Int): Either[String, Int] = ???

  def copy(
    name: Name = name,
    wiring: Wiring = wiring,
    notches: Vector[Position] = notches,
    ringSetting: Position): Either[String, Rotor] =
      Rotor.validate(name, wiring, notches, ringSetting)

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
      case v if v.distinct.length != 26 => Left("Wiring string must contain no duplicate letters.")
      case v if v.exists(c => c < 'A' || c > 'Z') => Left("Wiring string must contain only letters.")
      case v => Right(new Wiring(v))

  case class Name private (s: String)

  object Name:
    def apply(s: String): Either[String, String] = s.trim match
      case s if s.isEmpty => Left("Name cannot be blank.")
      case s => Right(s)

  private def apply(name: String, wiring: String, notches: Vector[Int], ringSetting: Int): Either[String, Rotor] =
    validate(name, wiring.toVector, notches, ringSetting)

  private def validateNotches(notches: Vector[Int]): ValidationResult[Vector[Int]] = notches match
    case v if v.length > RotorSize => "Notches must not be larger than rotor size.".invalidNec
    case v if v.length != v.distinct.length => "Notches must not contain duplicates.".invalidNec
    case v if v.exists(n => n < 0 || n >= RotorSize) => "Notch indices must be >= 0 and < rotor size.".invalidNec
    case v => v.validNec

  private def validate(
    name: String,
    wiring: Vector[Char],
    notches: Vector[Int],
    ringSetting: Int): Either[String, Rotor] = (
      validateName(name),
      validateWiring(wiring),
      validateNotches(notches),
      validateRingSetting(ringSetting))
        .mapN(Rotor.apply)
        .toEither
        .left
        .map(_.mkString_("\n"))

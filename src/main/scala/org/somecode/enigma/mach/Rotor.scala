package org.somecode.enigma
package mach

import cats._
import cats.implicits._
import cats.data._

case class Rotor private (
  name: Rotor.Name,
  wiring: Wiring,
  notches: Set[Position],
  ringSetting: Position)

object Rotor:

  val RotorSize = 26

  opaque type Name = String
  object Name:
    def apply(s: String): Either[String, Name] = s.trim match
      case s if s.isEmpty => Left("Name cannot be blank.")
      case s => Right(s)

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

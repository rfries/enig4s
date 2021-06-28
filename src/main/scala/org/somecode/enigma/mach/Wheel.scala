package org.somecode.enigma
package mach

sealed abstract case class Wheel private (
  wiring: Wiring,
  ringSetting: KeyCode,
  notches: Set[KeyCode]):
  //extends Machine.Bus:

  def size: Int = wiring.size

  // override def lookup(state: Machine.State, in: Position): Position =
  //   wiring.forward((in + ringSetting).toInt)

  // override def reverseLookup(state: Machine.State, in: Position): Position =
  //   wiring.reverse((in - ringSetting).toInt)

  def shouldAdvance(p: KeyCode): Boolean = notches.contains(p)

object Wheel:

  // def apply(s: String, ringSetting: KeyCode, notches: Set[KeyCode]): Either[String, Wheel] =
  //   Wiring.fromString(s).map(Wheel(_, ringSetting, notches))

  def validate(keyMap: Vector[KeyCode], ringSetting: KeyCode, notches: Set[KeyCode]): Either[String, Wheel] =
    keyMap match
      case v if v.size < 1 => Left(s"Wheel size must be > 0.")
      case v if ringSetting.toInt >= v.size => Left(s"Ring setting ($ringSetting) must be lower than the wiring size (${v.size}).")
      case v => Right(new Wheel(v, ringSetting, notches))

    // else if notches.
    // Wiring(v).map(Wheel(_, ringSetting, notches))

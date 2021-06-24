package org.somecode.enigma
package mach

case class Wheel(
  wiring: Wiring,
  ringSetting: KeyCode,
  notches: Set[KeyCode]):
  //extends Machine.Bus:

  // override def lookup(state: Machine.State, in: Position): Position =
  //   wiring.forward((in + ringSetting).toInt)

  // override def reverseLookup(state: Machine.State, in: Position): Position =
  //   wiring.reverse((in - ringSetting).toInt)

  def shouldAdvance(p: KeyCode): Boolean = notches.contains(p)

object Wheel:

  def apply(s: String, ringSetting: KeyCode, notches: Set[KeyCode]): Either[String, Wheel] =
    Wiring.fromString(s).map(Wheel(_, ringSetting, notches))

  def apply(v: Vector[KeyCode], ringSetting: KeyCode, notches: Set[KeyCode]): Either[String, Wheel] =
    Wiring.fromPositions(v).map(Wheel(_, ringSetting, notches))

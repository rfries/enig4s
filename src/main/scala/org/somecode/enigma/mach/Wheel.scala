package org.somecode.enigma
package mach

case class Wheel(
  wiring: Wiring,
  ringSetting: Position,
  notches: Set[Position]) extends Machine.Bus:

  override def lookup(state: Machine.State, in: Position): Position =
    wiring.forward((in + ringSetting).toInt)

  override def reverseLookup(state: Machine.State, in: Position): Position =
    wiring.reverse((in - ringSetting).toInt)

  def shouldAdvance(p: Position): Boolean = notches.contains(p)

object Wheel:

  def apply(s: String, ringSetting: Position, notches: Set[Position]): Either[String, Wheel] =
    Wiring.fromString(s).map(Wheel(_, ringSetting, notches))

  def apply(v: Vector[Position], ringSetting: Position, notches: Set[Position]): Either[String, Wheel] =
    Wiring.fromPositions(v).map(Wheel(_, ringSetting, notches))

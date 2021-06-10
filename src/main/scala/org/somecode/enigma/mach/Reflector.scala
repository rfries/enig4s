package org.somecode.enigma.mach

sealed abstract case class Reflector private (wiring: Wiring) extends Machine.Bus:

  override def lookup(state: Machine.State, in: Position): Position =
    wiring.forward(in)

  override def reverseLookup(state: Machine.State, in: Position): Position =
    wiring.reverse(in)

object Reflector:
  def apply(wiring: Wiring): Either[String, Reflector] =
    if wiring
      .forward
      .zipWithIndex
      .exists((p, idx) => p == idx)
    then Left("Wiring can't be used as a reflector, because there are some positions that map back to themselves.")
    else Right(new Reflector(wiring) {})

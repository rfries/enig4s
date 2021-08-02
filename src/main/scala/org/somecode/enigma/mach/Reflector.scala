package org.somecode.enigma
package mach

sealed abstract case class Reflector private (wiring: Wiring)
  extends Machine.Bus:

  override def size: Int = wiring.size

  override def translate(state: Machine.WheelState, in: KeyCode): KeyCode =
    wiring.forward(in.toInt)

  // override def reverseLookup(state: Machine.State, in: Position): Position =
  //   wiring.reverse(in.toInt)

object Reflector:
  def apply(wiring: Wiring): Either[String, Reflector] =
    if wiring
      .forward
      .zipWithIndex
      .exists((p, idx) => p.toInt == idx)
    then Left("Wiring can't be used as a reflector, because there are some positions that map back to themselves.")
    else Right(new Reflector(wiring) {})

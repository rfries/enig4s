package org.somecode.enigma
package mach

sealed abstract case class Wheel private (
  wiring: Wiring,
  ringSetting: KeyCode,
  notches: Set[KeyCode]):
  //extends Machine.Bus:

  val size: Int = wiring.size

  // override def lookup(state: Machine.State, in: Position): Position =
  //   wiring.forward((in + ringSetting).toInt)

  // override def reverseLookup(state: Machine.State, in: Position): Position =
  //   wiring.reverse((in - ringSetting).toInt)

  def notchedAt(p: KeyCode): Boolean = notches.contains(p)

object Wheel:

  def validate(wiring: Wiring, ringSetting: KeyCode, notches: Set[KeyCode]): Either[String, Wheel] =
    if (wiring.size < 1)
      Left(s"Wheel size must be > 0.")
    else if (ringSetting.toInt >= wiring.size)
      Left(s"Ring setting ($ringSetting) must be lower than the wiring size (${wiring.size}).")
    else if (notches.exists(k => k < 1 || k >= wiring.size))
      Left(s"Notch values must be between 0 and ${wiring.size-1}.")
    else
      Right(new Wheel(wiring, ringSetting, notches) {})

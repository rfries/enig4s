package org.somecode.enigma
package mach

sealed abstract case class Wheel private (
  xlate: Translation,
  ringSetting: KeyCode,
  notches: Set[KeyCode]):
  //extends Machine.Bus:

  val size: Int = xlate.size

  // override def lookup(state: Machine.State, in: Position): Position =
  //   wiring.forward((in + ringSetting).toInt)

  // override def reverseLookup(state: Machine.State, in: Position): Position =
  //   wiring.reverse((in - ringSetting).toInt)

  def shouldAdvance(p: KeyCode): Boolean = notches.contains(p)

object Wheel:

  def validate(xlate: Translation, ringSetting: KeyCode, notches: Set[KeyCode]): Either[String, Wheel] =
    if xlate.size < 1 then Left(s"Wheel size must be > 0.")
    else if ringSetting.toInt >= xlate.size then Left(s"Ring setting ($ringSetting) must be lower than the wiring size (${xlate.size}).")
    else if notches.exists(k => k.toInt < 1 || k.toInt >= xlate.size) then Left(s"Notch values must be between 1 and ${xlate.size-1}.")
    else Right(new Wheel(xlate, ringSetting, notches))

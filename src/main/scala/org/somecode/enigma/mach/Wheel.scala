package org.somecode.enigma
package mach

import Machine.*

sealed case class ConfiguredWheel(
  val ringSetting: KeyCode,
  val wheel: Wheel) extends Machine.Bus:

  val size: Int = wheel.size

  def translate(state: WState, key: KeyCode): KeyCode =
    KeyCode.unsafe(
      wheel.wiring.forward((key + state.position + ringSetting) % size)
    )

  def cotranslate(state: WState, key: KeyCode): KeyCode =
    KeyCode.unsafe(
      (wheel.wiring.reverse(key) - state.position - ringSetting) % size
    )

sealed abstract case class Wheel private (
  wiring: Wiring,
  notches: Set[KeyCode]):
  //extends Machine.Bus:

  val size: Int = wiring.size

  def copy(
    wiring: Wiring = wiring,
    notches: Set[KeyCode] = notches): Either[String, Wheel] =
      Wheel.apply(wiring, notches)

  def configure(setting: KeyCode): Either[String, ConfiguredWheel] =
    Either.cond(setting < size,
      new ConfiguredWheel(setting, this) {},
      s"Ring setting ($setting) must be between 0 and ${size-1}")

  // override def lookup(state: Machine.State, in: Position): Position =
  //   wiring.forward((in + ringSetting).toInt)

  // override def reverseLookup(state: Machine.State, in: Position): Position =
  //   wiring.reverse((in - ringSetting).toInt)

  // def notchedAt(p: KeyCode): Boolean = notches.contains(p)

object Wheel:

  def apply(wiring: Wiring, notches: Set[KeyCode]): Either[String, Wheel] =
    if (wiring.size < 1)
      Left(s"Wheel size must be > 0.")
    // else if (ringSetting.toInt >= wiring.size)
    //   Left(s"Ring setting ($ringSetting) must be lower than the wiring size (${wiring.size}).")
    else if (notches.exists(_ >= wiring.size))
      Left(s"Notch values must be between 0 and ${wiring.size-1}.")
    else
      Right(new Wheel(wiring, notches) {})

  def apply(letterMap: String, notches: Set[String]): Either[String, Wheel] =
    for
      wiring <- Wiring.fromString(letterMap)
      notchCodes <- validateNotches(wiring.size, notches)
      wheel <- Wheel(wiring, notchCodes)
    yield
      wheel

  def validateNotches(wheelSize: Int, notches: Set[String]): Either[String, Set[KeyCode]] =
    if (wheelSize < 1 || wheelSize > 26)
      Left("Notch specifier strings only supported for wheel sizes up to 26.")
    else if (notches.exists(_.size != 1))
      Left("All notch strings must have a length of 1.")
    else
      val notchCodes = notches.map(s => s(0) - 'A')
      if (notchCodes.exists(n => n < 0 || n >= wheelSize))
        Left("Notch specifications must be between 'A' and 'Z'.")
      else
        Right(notchCodes.map(KeyCode.unsafe))

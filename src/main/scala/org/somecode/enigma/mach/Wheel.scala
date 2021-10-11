package org.somecode.enigma
package mach

import Machine.*

sealed case class ConfiguredWheel(
  val ringSetting: KeyCode,
  val wheel: Wheel) extends Rotor:

  val size: Int = wheel.size

  override def translate(wheelNum: Int, state: WheelState, in: KeyCode): KeyCode =

    val out = wheel
      .wiring
      .translate(
        in.plusMod(size, state.position).minusMod(size, ringSetting)
      )
      .minusMod(size, state.position)
      .plusMod(size, ringSetting)

    println(f"$wheelNum: [${state.position}] $in%02d (${(in + 'A').toChar}) -> $out%02d (${(out + 'A').toChar})")
    out

  override def cotranslate(wheelNum: Int, state: WheelState, in: KeyCode): KeyCode =
    val out = wheel
      .wiring
      .cotranslate(in.plusMod(size, state.position).minusMod(size, ringSetting))
      .minusMod(size, state.position)
      .plusMod(size, ringSetting)

    println(f"$wheelNum: [${state.position}] $out%02d (${(out + 'A').toChar}) <- $in%02d (${(in + 'A').toChar})")
    out

sealed abstract case class Wheel private (
  wiring: Wiring,
  notches: Set[KeyCode]):

  val size: Int = wiring.size

  def copy(
    wiring: Wiring = wiring,
    notches: Set[KeyCode] = notches): Either[String, Wheel] =
      Wheel.apply(wiring, Notches(notches))

  def configure(setting: KeyCode): Either[String, ConfiguredWheel] =
    Either.cond(setting < size,
      new ConfiguredWheel(setting, this) {},
      s"Ring setting ($setting) must be between 0 and ${size-1}")

  def notchedAt(p: KeyCode): Boolean = notches.contains(p)

object Wheel:

  def apply(wiring: Wiring, notches: Notches): Either[String, Wheel] =
    if (wiring.size < 1)
      Left(s"Wheel size must be > 0.")
    else if (notches.notches.exists(_ >= wiring.size))
      Left(s"Notch values must be between 0 and ${wiring.size-1}.")
    else
      Right(new Wheel(wiring, notches.notches) {})

  def apply(letterMap: String, notches: Set[String]): Either[String, Wheel] =
    for
      wiring <- Wiring.fromString(letterMap)
      notchCodes <- validateNotches(wiring.size, notches)
      wheel <- Wheel(wiring, Notches(notchCodes))
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

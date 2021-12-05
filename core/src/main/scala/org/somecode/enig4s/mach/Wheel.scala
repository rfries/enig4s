package org.somecode.enig4s
package mach

import cats.implicits.*
import Machine.*

sealed abstract case class Wheel private (
  wiring: Wiring,
  notches: Set[KeyCode],
  ringSetting: KeyCode
) extends Rotor:
  val size: Int = wiring.size

  override def translate(wheelNum: Int, state: WheelState, in: KeyCode): KeyCode =
    val out = wiring
      .translate(
        in.plusMod(size, state.position).minusMod(size, ringSetting)
      )
      .minusMod(size, state.position)
      .plusMod(size, ringSetting)

    println(f"$wheelNum: [${state.position}%02d] $in%02d (${(in + 'A').toChar}) -> $out%02d (${(out + 'A').toChar})")
    out

  override def reverseTranslate(wheelNum: Int, state: WheelState, in: KeyCode): KeyCode =
    val out = wiring
      .reverseTranslate(
        in.plusMod(size, state.position).minusMod(size, ringSetting)
      )
      .minusMod(size, state.position)
      .plusMod(size, ringSetting)

    println(f"$wheelNum: [${state.position}%02d] $out%02d (${(out + 'A').toChar}) <- $in%02d (${(in + 'A').toChar})")
    out

  def copy(
    wiring: Wiring = wiring,
    notches: Set[KeyCode] = notches,
    ringSetting: KeyCode
  ): Either[String, Wheel] = Wheel.apply(wiring, notches, ringSetting)

  def notchedAt(p: KeyCode): Boolean = notches.contains(p)

object Wheel:

  def apply(wiring: Wiring, notches: Set[KeyCode], ringSetting: KeyCode): Either[String, Wheel] =
    if wiring.size < 1 then
      Left(s"Wheel size must be > 0.")
    else if notches.exists(_ >= wiring.size) then
      Left(s"Notch values must be between 0 and ${wiring.size-1}.")
    else if ringSetting >= wiring.size then
      Left(s"Ring setting must be between 0 (inclusive) and ${wiring.size} (exclusive).")
    else
      Right(new Wheel(wiring, notches, ringSetting) {})

  def apply(wiring: Wiring, notches: String, ringSetting: KeyCode): Either[String, Wheel] =
    for
      notchCodes <- validateNotches(wiring.size, notches)
      wheel <- Wheel(wiring, notchCodes, KeyCode.unsafe(ringSetting))
    yield
      wheel

  def apply(letterMap: String, notches: String, ringSetting: KeyCode): Either[String, Wheel] =
    for
      wiring <- Wiring(letterMap)
      notchCodes <- validateNotches(wiring.size, notches)
      wheel <- apply(wiring, notchCodes, ringSetting)
    yield
      wheel

  def validateNotches(wheelSize: Int, notches: String): Either[String, Set[KeyCode]] =
    if (wheelSize < 1 || wheelSize > 26)
      Left("Notch specifier strings only supported for wheel sizes up to 26.")
    else if (notches.size != notches.distinct.size)
      Left("Notch specifier cannot contain duplicate symbols.")
    else if (notches.size > wheelSize)
      Left(s"Notch specifier length (${notches.size}) cannot be greater than wheel size ($wheelSize).")
    else
      val notchCodes = notches.map(_ - 'A').toSet
      if notchCodes.exists(n => n < 0 || n >= wheelSize) then
        Left(s"Notch specifications must be between 0 and $wheelSize.")
      else
        Right(notchCodes.map(KeyCode.unsafe))

  // def configureWheels(wheelConfigs: (Wheel, Char)*): Either[String, Seq[ConfiguredWheel]] =
  //   wheelConfigs.reverse.map { (wheel, setting) =>
  //     if setting < 1 || setting >= wheel.size then
  //       Left(s"Wheel setting (${setting.toInt}) must be between 0 and ${wheel.size-1}.")
  //     else
  //       wheel.configure(KeyCode.unsafe(setting - 'A'))
  //   }.sequence


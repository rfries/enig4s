package org.somecode.enig4s
package mach

import cats.implicits.*
import org.somecode.enig4s.mach.Machine.*

sealed abstract case class Wheel private (
  wiring: Wiring,
  notches: IndexedSeq[KeyCode]
  //ringSetting: KeyCode
) extends Rotor:
  val size: Int = wiring.size

  override def translate(wheelNum: Int, state: WheelState, in: KeyCode): KeyCode =
    val out: KeyCode = wiring
      .translate(
        in.plusMod(size, state.position).minusMod(size, state.ringSetting: Int)
      )
      .minusMod(size, state.position)
      .plusMod(size, state.ringSetting)

    println(f"$wheelNum: [${state.position}%02d] $in%02d (${(in + 'A').toChar}) -> $out%02d (${(out + 'A').toChar})")
    out

  override def reverseTranslate(wheelNum: Int, state: WheelState, in: KeyCode): KeyCode =
    val out: KeyCode = wiring
      .reverseTranslate(
        in.plusMod(size, state.position).minusMod(size, state.ringSetting)
      )
      .minusMod(size, state.position)
      .plusMod(size, state.ringSetting)

    println(f"$wheelNum: [${state.position}%02d] $out%02d (${(out + 'A').toChar}) <- $in%02d (${(in + 'A').toChar})")
    out

  def copy(
    wiring: Wiring = wiring,
    notches: IndexedSeq[KeyCode] = notches
  ): Either[String, Wheel] = Wheel.apply(wiring, notches)

  def notchedAt(p: KeyCode): Boolean = notches.contains(p)

object Wheel:

  def apply(wiring: Wiring, notches: IndexedSeq[KeyCode]): Either[String, Wheel] =
    if wiring.size < 1 then
      Left(s"Wheel size must be > 0.")
    else if notches.exists(_ >= wiring.size) then
      Left(s"Notch values must be between 0 and ${wiring.size-1}.")
//    else if ringSetting >= wiring.size then
//      Left(s"Ring setting must be between 0 (inclusive) and ${wiring.size} (exclusive).")
    else
      Right(new Wheel(wiring, notches) {})

  def apply(wiring: Wiring, notches: String, symbols: SymbolMap): Either[String, Wheel] =
    for
      notchCodes <- symbols.stringToCodes(notches)
      validNotches <- validateNotches(wiring.size, notchCodes)
      wheel <- Wheel(wiring, validNotches)
    yield
      wheel

  // def apply(letterMap: String, notches: String, ringSetting: KeyCode): Either[String, Wheel] =
  //   for
  //     wiring <- Wiring(letterMap)
  //     notchCodes <- validateNotches(wiring.size, notches)
  //     wheel <- apply(wiring, notchCodes, ringSetting)
  //   yield
  //     wheel

  def validateNotches(wheelSize: Int, notchCodes: IndexedSeq[KeyCode]): Either[String, IndexedSeq[KeyCode]] =
    if notchCodes.length != notchCodes.distinct.length then
      Left("Notch specifier cannot contain duplicate symbols.")
    else if notchCodes.length > wheelSize then
      Left(s"Notch specifier length (${notchCodes.length}) cannot be greater than wheel size ($wheelSize).")
    else if notchCodes.exists(_ >= wheelSize) then
      Left(s"Notch codes must be between 0 and $wheelSize (exclusive).")
    else
      Right(notchCodes.map(KeyCode.unsafe))

  // def configureWheels(wheelConfigs: (Wheel, Char)*): Either[String, Seq[ConfiguredWheel]] =
  //   wheelConfigs.reverse.map { (wheel, setting) =>
  //     if setting < 1 || setting >= wheel.size then
  //       Left(s"Wheel setting (${setting.toInt}) must be between 0 and ${wheel.size-1}.")
  //     else
  //       wheel.configure(KeyCode.unsafe(setting - 'A'))
  //   }.sequence

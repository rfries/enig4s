package org.somecode.enig4s
package mach

import cats.implicits.*

sealed abstract case class Wheel private (
  wiring: Wiring,
  notches: Set[KeyCode]
) extends Rotor:

  val size: Int = wiring.size

  def forward(state: WheelState): KeyCode => KeyCode = in =>
    minusMod(wiring.forward(plusMod(in, state)), state)

  def reverse(state: WheelState): KeyCode => KeyCode = in =>
    minusMod(wiring.reverse(plusMod(in, state)), state)

  def copy(
    wiring: Wiring = wiring,
    notches: Set[KeyCode] = notches
  ): Either[String, Wheel] = Wheel.apply(wiring, notches.toSeq)

  def notchedAt(p: KeyCode): Boolean = notches.contains(p)

object Wheel:

  def apply(wiring: Wiring, notches: Seq[KeyCode]): Either[String, Wheel] =
    if wiring.size < 1 then
      Left(s"Wheel size must be > 0.")
    validateNotches(wiring.size, notches).map(ns => new Wheel(wiring, ns.toSet) {})

  def apply(wiring: Wiring, notches: String, symbols: SymbolMap): Either[String, Wheel] =
    for
      notchCodes <- symbols.stringToCodes(notches)
      validNotches <- validateNotches(wiring.size, notchCodes)
    yield
      new Wheel(wiring, validNotches) {}

  def validateNotches(wheelSize: Int, notchCodes: Seq[KeyCode]): Either[String, Set[KeyCode]] =
    if notchCodes.length != notchCodes.distinct.length then
      Left("Notch specifier cannot contain duplicate symbols.")
    else if notchCodes.length > wheelSize then
      Left(s"Notch specifier length (${notchCodes.length}) cannot be greater than wheel size ($wheelSize).")
    else if notchCodes.exists(_ >= wheelSize) then
      Left(s"Notch codes must be between 0 and $wheelSize (exclusive).")
    else
      Right(notchCodes.map(KeyCode.unsafe).toSet)

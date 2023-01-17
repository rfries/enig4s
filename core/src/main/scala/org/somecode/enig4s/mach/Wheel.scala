 package org.somecode.enig4s
package mach

import Trace.*

sealed abstract case class Wheel private (
  wiring: Wiring,
  notches: Set[Glyph],
  wheelNum: Int
) extends Bidirectional:

  import wiring.modulus

  val length: Int = wiring.length
  val forward: Transformer = transformer(wiring, Direction.Forward)
  val reverse: Transformer = transformer(wiring.inverse, Direction.Reverse)

  // Due to the physical geometry of the wheels, the position is
  // additive and the ring setting is subtractive.  They are applied
  // before looking up the value via the wiring, and then unapplied
  // to the result.
  def transformer(wires: Wiring, direction: Direction): Transformer = (state, glyph) =>
      val pos = state.positions(wheelNum)
      val ring = state.rings(wheelNum)
      val off = pos %- ring
      val out = wires.wire(glyph %+ off) %- off
      Trace.trace(state, glyph, out, Component.Wheel(wheelNum),
        s"pos ${state.symbols.displayGlyph(pos)} ring ${state.symbols.displayGlyph(ring)}")

  def notchedAt(p: Glyph): Boolean = notches.contains(p)

  def copy(
    wiring: Wiring = wiring,
    notches: Set[Glyph] = notches,
    wheelNum: Int = wheelNum
  ): Either[String, Wheel] = Wheel.apply(wiring, notches.toSeq, wheelNum)

object Wheel:

  def apply(wiring: Wiring, notches: Seq[Glyph], wheelNum: Int): Either[String, Wheel] =
    if wiring.length < 1 then
      Left(s"Wheel size must be > 0.")
    else if wheelNum < 0  then
      Left(s"Wheel number ($wheelNum) must be 0 or more")
    else
      validateNotches(wiring.length, notches).map(ns => new Wheel(wiring, ns.toSet, wheelNum) {})

  def apply(wiring: Wiring, notches: String, ring: String, wheelNum: Int, symbols: SymbolMap): Either[String, Wheel] =
    for
      notchCodes <- symbols.stringToGlyphs(notches)
      wn <- Either.cond(wheelNum < 1, wheelNum, "Wheel number must be > 0")
      validNotches <- validateNotches(wiring.length, notchCodes)
    yield
      new Wheel(wiring, validNotches, wn) {}

  def validateNotches(wheelSize: Int, notchCodes: Seq[Glyph]): Either[String, Set[Glyph]] =
    if notchCodes.length != notchCodes.distinct.length then
      Left("Notch specifier cannot contain duplicate symbols.")
    else if notchCodes.length > wheelSize then
      Left(s"Notch specifier length (${notchCodes.length}) cannot be greater than wheel size ($wheelSize).")
    else if notchCodes.exists(_.toInt >= wheelSize) then
      Left(s"Notch codes must be between 0 and $wheelSize (exclusive).")
    else
      Right(notchCodes.toSet)

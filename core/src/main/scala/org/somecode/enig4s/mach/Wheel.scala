package org.somecode.enig4s
package mach

import Trace.*

sealed abstract case class Wheel private (
  wiring: Wiring,
  notches: Set[Glyph],
  ring: Glyph,
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
      val pos = state.wheelState(wheelNum)
      val off = pos %- ring
      val out = wires.wire(glyph %+ off) %- off
      Trace.trace(state, glyph, out, Component.Wheel(wheelNum), direction,
        s"pos: ${state.symbols.displayCode(pos)}, ring: ${state.symbols.displayCode(pos)}")

  def notchedAt(p: Glyph): Boolean = notches.contains(p)

  def copy(
    wiring: Wiring = wiring,
    notches: Set[Glyph] = notches,
    ring: Glyph = ring,
    wheelNum: Int = wheelNum
  ): Either[String, Wheel] = Wheel.apply(wiring, notches.toSeq, ring, wheelNum)

object Wheel:

  def apply(wiring: Wiring, notches: Seq[Glyph], ring: Glyph, wheelNum: Int): Either[String, Wheel] =
    if wiring.length < 1 then
      Left(s"Wheel size must be > 0.")
    else if ring.toInt >= wiring.length then
      Left(s"Ring setting (${ring.toInt}) is too large for wheel size (${wiring.length})")
    else if wheelNum < 0  then
      Left(s"Wheel number ($wheelNum) must be 0 or more")
    else
      validateNotches(wiring.length, notches).map(ns => new Wheel(wiring, ns.toSet, ring, wheelNum) {})

  def apply(wiring: Wiring, notches: String, ring: String, wheelNum: Int, symbols: SymbolMap): Either[String, Wheel] =
    for
      notchCodes <- symbols.stringToGlyphs(notches)
      rs <- validateRingSetting(ring, symbols)
      wn <- Either.cond(wheelNum < 1, wheelNum, "Wheel number must be > 0")
      validNotches <- validateNotches(wiring.length, notchCodes)
    yield
      new Wheel(wiring, validNotches, rs, wn) {}

  def validateNotches(wheelSize: Int, notchCodes: Seq[Glyph]): Either[String, Set[Glyph]] =
    if notchCodes.length != notchCodes.distinct.length then
      Left("Notch specifier cannot contain duplicate symbols.")
    else if notchCodes.length > wheelSize then
      Left(s"Notch specifier length (${notchCodes.length}) cannot be greater than wheel size ($wheelSize).")
    else if notchCodes.exists(_.toInt >= wheelSize) then
      Left(s"Notch codes must be between 0 and $wheelSize (exclusive).")
    else
      Right(notchCodes.toSet)

  def validateRingSetting(rs: String, symbols: SymbolMap): Either[String, Glyph] =
    if (rs.length() != 1)
      Left("Ring setting string length is not 1.")
    else
      symbols.pointToGlyph(rs.codePointAt(0))


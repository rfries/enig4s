package org.somecode.enig4s
package mach

//import cats.implicits.*

sealed abstract case class Wheel private (
  wiring: Wiring,
  ring: Glyph,
  wheelNum: Int,
  notches: Set[Glyph]
):
  import wiring.modulus

  val length: Int = wiring.length

  val forward: Transformer = transformer(wiring)
  val reverse: Transformer = transformer(wiring.inverse)

  def transformer(wires: Wiring): Transformer = (state, glyph) =>
      val pos = state.wheelState(wheelNum)
      val off = pos %- ring
      val out = wires.wire(glyph %+ off) %- off
      (state, out)

  def notchedAt(p: Glyph): Boolean = notches.contains(p)

  def copy(
    wiring: Wiring = wiring,
    ring: Glyph,
    wheelNum: Int,
    notches: Set[Glyph] = notches
  ): Either[String, Wheel] = Wheel.apply(wiring, ring, wheelNum, notches.toSeq)

object Wheel:

  def apply(wiring: Wiring, ring: Glyph, wheelNum: Int, notches: Seq[Glyph]): Either[String, Wheel] =
    if wiring.length < 1 then
      Left(s"Wheel size must be > 0.")
    else if ring.toInt >= wiring.length then
      Left(s"Ring setting (${ring.toInt}) is too large for wheel size (${wiring.length})")
    else if wheelNum < 0  then
      Left(s"Wheel number ($wheelNum) must be 0 or more")
    else
      validateNotches(wiring.length, notches).map(ns => new Wheel(wiring, ring, wheelNum, ns.toSet) {})

  def apply(wiring: Wiring, ring: String, wheelNum: Int, notches: String, symbols: SymbolMap): Either[String, Wheel] =
    for
      notchCodes <- symbols.stringToGlyphs(notches)
      rs <- validateRingSetting(ring, symbols)
      wn <- Either.cond(wheelNum < 1, wheelNum, "Wheel number must be > 0")
      validNotches <- validateNotches(wiring.length, notchCodes)
    yield
      new Wheel(wiring, rs, wn, validNotches) {}

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


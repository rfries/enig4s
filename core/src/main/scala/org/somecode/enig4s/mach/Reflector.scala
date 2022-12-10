package org.somecode.enig4s
package mach

import cats.implicits.*
import Trace.*

sealed abstract case class Reflector private (
  wiring: Wiring,
  positions: Set[Glyph],
):
  import wiring.modulus

  val length: Int = wiring.length

  val reflect: Transformer = (state, in) =>
      val pos = state.reflectorState
      val out = wiring.wire(in %+ pos) %- pos
      Trace.trace(state, in, out, Component.Reflector, Trace.Direction.Reflect, s"pos: ${state.symbols.displayCode(pos)}")

  def validPosition(g: Glyph): Boolean = positions.contains(g)

object Reflector:

  def apply(
    wiring: Wiring,
    positions: Set[Glyph] = Set(Glyph.zero)
  ): Either[String, Reflector] =

    if wiring.wiring.zipWithIndex.exists((k, idx) => k.toInt === idx) then
      Left("Wiring can't be used as a reflector, because there are some positions that map back to themselves")
    else if positions.size > wiring.length then
      Left(s"Position list is too large (${positions.size}) for bus size (${wiring.length})")
    else if positions.exists(g => g.toInt < 0 || g.toInt >= wiring.length) then
      Left(s"Allowed position list must contain only values from 0 (inclusive) to the wiring length ${wiring.length} (exclusive).")
    else
      Right(new Reflector(wiring, positions) {})

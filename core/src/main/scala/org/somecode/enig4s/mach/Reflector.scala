package org.somecode.enig4s
package mach

import cats.implicits.*

sealed abstract case class Reflector private (
  wiring: Wiring,
  positions: Option[Set[Glyph]],
) extends Transformer:

  import wiring.modulus

  override val transformer: (MachineState, Glyph) => (MachineState, Glyph) =
    (state, in) =>
      val pos = state.reflectorState
      val out = wiring.wire(in +% pos) -% pos
      (state, out)

  // def forward(pos: Position): KeyCode => KeyCode = in =>
  //   minusMod(wiring.codes(plusMod(in, pos)), pos)

object Reflector:

  def apply(
    wiring: Wiring,
    positions: Option[Seq[Glyph]] = None
  ): Either[String, Reflector] =

    if wiring.wiring.zipWithIndex.exists((k, idx) => k.toInt === idx) then
      Left("Wiring can't be used as a reflector, because there are some positions that map back to themselves")
    else
      positions.map(pos =>
        if pos.length > wiring.length then
          Left(s"Position list is too large (${pos.size}) for bus size (${wiring.length})")
        else if pos.distinct.length =!= pos.length then
          Left(s"Position list contains duplicates")
        else if pos.exists(g => g.toInt < 0 || g.toInt >= wiring.length) then
          Left(s"Allowed position list must contain only values from 0 (inclusive) to the wiring length ${wiring.length} (exclusive).")
        else
          Right(pos).map(g => new Reflector(wiring, Some(g.toSet)) {})
      )
      .getOrElse(Right(new Reflector(wiring, None) {}))

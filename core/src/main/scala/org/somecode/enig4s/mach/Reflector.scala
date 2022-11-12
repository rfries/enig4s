package org.somecode.enig4s
package mach

import cats.implicits.*

sealed abstract case class Reflector private (
  wiring: Wiring,
  positions: Option[Set[Position]],
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
    positions: Option[Seq[Position]] = None
  ): Either[String, Reflector] =

    if wiring.wiring.zipWithIndex.exists((k, idx) => k.toInt === idx) then
      Left("Wiring can't be used as a reflector, because there are some positions that map back to themselves")
    else
      positions.map(pos =>
        if (pos.size > wiring.length)
          Left(s"Position list is too large (${pos.size}) for bus size (${wiring.length})")
        else if (pos.distinct.size =!= pos.size)
          Left(s"Position list contains duplicates")
        else
          Right(pos).map(p => new Reflector(wiring, Some(p.toSet)) {})
      )
      .getOrElse(Right(new Reflector(wiring, None) {}))

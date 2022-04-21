package org.somecode.enig4s
package mach

import cats.implicits.*

sealed abstract case class Reflector private (
  wiring: Wiring,
  positions: Option[IndexedSeq[Position]],
) extends Rotor:

  def size: Int = wiring.size

  def forward(state: WheelState): KeyCode => KeyCode = in =>
    val out = wiring.codes(plusMod(in, state))
    println(f"r: [${state.position}%02d] $in%02d (${(in + 'A').toChar}) --> $out%02d (${(out + 'A').toChar})")
    out

object Reflector:

  def apply(
    wiring: Wiring,
    positions: Option[IndexedSeq[Position]] = None,
  ): Either[String, Reflector] =
    if (wiring.codes.zipWithIndex.exists((k, idx) => k.toInt === idx))
      Left("Wiring can't be used as a reflector, because there are some positions that map back to themselves")
    else
      positions.map(pos =>
        if (pos.size > wiring.size)
          Left(s"Position list is too large (${pos.size}) for bus size (${wiring.size})")
        else if (pos.distinct.size =!= pos.size)
          Left(s"Position list contains duplicates")
        else
          Right(pos).map(p => new Reflector(wiring, Some(p)) {})
      )
      .getOrElse(Right(new Reflector(wiring, None) {}))

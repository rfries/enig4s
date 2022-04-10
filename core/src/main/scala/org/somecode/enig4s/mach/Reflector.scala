package org.somecode.enig4s
package mach

import cats.implicits.*

sealed abstract case class Reflector private (
  busSize: BusSize,
  wiring: Wiring,
  positions: IndexedSeq[Position]
) extends Rotor:

  def forward(state: WheelState): KeyCode => KeyCode = in =>
    val out = wiring.codes(plusMod(in, state))
    println(f"r: [${state.position}%02d] $in%02d (${(in + 'A').toChar}) --> $out%02d (${(out + 'A').toChar})")
    out

object Reflector:

  def apply(
    size: Int,
    wiring: Wiring,
    positions: IndexedSeq[Position] = Vector(Position.zero)
  ): Either[String, Reflector] =
    BusSize(size).flatMap { sz =>
      if (size =!= wiring.size)
        Left(s"Wiring size (${wiring.size}) does not match bus size ($sz)")
      else if (wiring.codes.zipWithIndex.exists((k, idx) => k.toInt === idx))
        Left("Wiring can't be used as a reflector, because there are some positions that map back to themselves")
      else if (positions.size > sz || positions.distinct.size =!= positions.size)
        Left(s"Position list is too large (${positions.size}) for bus, or contains duplicates ($sz)")
      else
        Right(new Reflector(sz, wiring, positions) {})
    }

package org.somecode.enig4s
package mach

import cats.implicits.*

sealed abstract case class Reflector private (
  wiring: Wiring,
  positions: IndexedSeq[Position],
  advance: Boolean
):

  def size: Int = wiring.size

  def forward(state: WheelState): KeyCode => KeyCode = in =>
    val out = wiring.codes(in.plusMod(size, state.position))
    println(f"r: [${state.position}%02d] $in%02d (${(in + 'A').toChar}) --> $out%02d (${(out + 'A').toChar})")
    out

  def translate(state: WheelState, in: KeyCode): KeyCode =
    val out = wiring.codes(in.plusMod(size, state.position))
    println(f"r: [${state.position}%02d] $in%02d (${(in + 'A').toChar}) --> $out%02d (${(out + 'A').toChar})")
    out

object Reflector:

  def apply(
    wiring: Wiring,
    positions: IndexedSeq[Position] = Vector.empty,
    advance: Boolean = false
  ): Either[String, Reflector] =

    if wiring.codes.zipWithIndex.exists((p, idx) => p.toInt === idx)
    then Left("Wiring can't be used as a reflector, because there are some positions that map back to themselves.")
    else Right(new Reflector(wiring, positions, advance) {})

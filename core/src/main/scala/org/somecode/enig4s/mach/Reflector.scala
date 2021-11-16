package org.somecode.enig4s
package mach

import cats.implicits.*
import org.somecode.enig4s.mach.Machine.{MachineState, WheelState}

sealed abstract case class Reflector private (wiring: Wiring):

  def size: Int = wiring.size

  def translate(state: WheelState, in: KeyCode): KeyCode =
    val out = wiring.forward(in.plusMod(size, state.position))
    println(f"r: [${state.position}%02d] $in%02d (${(in + 'A').toChar}) --> $out%02d (${(out + 'A').toChar})")
    out

object Reflector:
  def apply(wiring: Wiring): Either[String, Reflector] =
    if wiring
      .forward
      .zipWithIndex
      .exists((p, idx) => p.toInt === idx)
    then Left("Wiring can't be used as a reflector, because there are some positions that map back to themselves.")
    else Right(new Reflector(wiring) {})

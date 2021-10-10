package org.somecode.enigma
package mach

import cats.implicits.*
import org.somecode.enigma.mach.Machine.{MachineState, WheelState}

sealed abstract case class Reflector private (wiring: Wiring)
  extends Machine.Rotor:

  override def size: Int = wiring.size

  override def translate(state: WheelState, in: KeyCode): KeyCode =
    val out = wiring.forward(in.plusMod(size, state.position))
    println(s"[${state.position}] $in --> $out")
    out

  override def cotranslate(state: WheelState, in: KeyCode): KeyCode =
    throw new RuntimeException("Cotranslation unsupported in reflectors.")

object Reflector:
  def apply(wiring: Wiring): Either[String, Reflector] =
    if wiring
      .forward
      .zipWithIndex
      .exists((p, idx) => p.toInt === idx)
    then Left("Wiring can't be used as a reflector, because there are some positions that map back to themselves.")
    else Right(new Reflector(wiring) {})

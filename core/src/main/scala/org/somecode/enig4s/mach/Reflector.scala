package org.somecode.enig4s
package mach

import cats.data.Writer
import cats.implicits.*
import Trace.*

sealed abstract case class Reflector private (wiring: Wiring):

  import wiring.modulus

  val length: Int = wiring.length

  val reflect: Transformer = (state, in) =>
    val pos = state.reflector
    val out = wiring.wire(in %+ pos) %- pos
    Writer(Trace.trace(state, in, out, Component.Reflector, s"pos ${state.symbols.displayGlyph(pos)}"), out)

object Reflector:

  def apply(wiring: Wiring): Either[String, Reflector] =
    if wiring.wiring.zipWithIndex.exists((k, idx) => k.toInt === idx) then
      Left("Wiring can't be used as a reflector, because there are some positions that map back to themselves")
    else
      Right(new Reflector(wiring) {})

package org.somecode.enig4s
package mach

import Trace.*

/**
 * An [[Entry]] disk is a trivial wrapper around a static [[Wiring]],
 * providing a forward and reverse [[Transformer]].
 *
 * @param wiring The wiring for which to provide bidirectional [[Transformer]]s.
 */
final case class Entry (wiring: Wiring) extends Bidirectional:

  val length: Int = wiring.length

  val forward: Transformer = (state, in) =>
    trace(state, in, wiring.wire(in), Direction.Forward)
  val reverse: Transformer = (state, in) =>
    trace(state, in, wiring.inverse.wire(in), Direction.Reverse)

  def trace(state: MachineState, in: Glyph, out: Glyph, direction: Direction): (MachineState, Glyph) =
    Trace.trace(state, in, out, Component.EntryDisc, direction)

object Entry:

  def passthrough(n: Int): Either[String, Entry] =
    Wiring.passthrough(n).map(Entry.apply)

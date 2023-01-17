package org.somecode.enig4s
package mach

import Trace.*

/**
 * An [[Entry]] disk is a simple wrapper around a static [[Wiring]],
 * providing a forward and reverse [[Transformer]].
 *
 * @param wiring The wiring for which to provide bidirectional [[Transformer]]s.
 */
final case class Entry (wiring: Wiring) extends Bidirectional:

  val length: Int = wiring.length

  val forward: Transformer = (state, in) =>
    Trace.trace(state, in, wiring.wire(in), Component.EntryDisc(Direction.Forward))

  val reverse: Transformer = (state, in) =>
    Trace.trace(state, in, wiring.inverse.wire(in), Component.EntryDisc(Direction.Reverse))

object Entry:

  def passthrough(n: Int): Either[String, Entry] =
    Wiring.passthrough(n).map(Entry.apply)

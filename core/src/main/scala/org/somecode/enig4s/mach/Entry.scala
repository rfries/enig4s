package org.somecode.enig4s
package mach

import cats.data.Writer

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
    val out = wiring.wire(in)
    Writer(Trace.trace(state, in, out, Component.EntryDisc(Direction.Forward)), out)

  val reverse: Transformer = (state, in) =>
    val out = wiring.inverse.wire(in)
    Writer(Trace.trace(state, in, out, Component.EntryDisc(Direction.Reverse)), out)

object Entry:

  def passthrough(n: Int): Either[String, Entry] =
    Wiring.passthrough(n).map(Entry.apply)

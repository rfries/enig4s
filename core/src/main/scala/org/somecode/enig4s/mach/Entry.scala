package org.somecode.enig4s
package mach

/**
 * An [[Entry]] disk is a trivial wrapper around a static [[Wiring]],
 * providing a forward and reverse [[Transformer]].
 *
 * @param wiring The wiring for which to provide bidirectional [[Transformer]]s.
 */
final case class Entry (wiring: Wiring) extends Bidirectional:
  val length: Int = wiring.length
  val forward: Transformer = (state, in) => (state, wiring.wire(in))
  val reverse: Transformer = (state, in) => (state, wiring.inverse.wire(in))

object Entry:
  def passthrough(n: Int): Either[String, Entry] =
    Wiring.passthrough(n).map(Entry.apply)

package org.somecode.enig4s
package mach

final case class Entry private (
  wiring: Wiring
):
  given modulus: Modulus = wiring.modulus

  val length: Int = wiring.length
  val forward: Transformer = (state, in) => (state, wiring.wire(in))
  val reverse: Transformer = (state, in) => (state, wiring.inverse.wire(in))

object Entry:
  def apply(wiring: Wiring): Either[String, Entry] =
    Either.cond(
      wiring.length > 0,
      new Entry(wiring),
      s"Wiring length (${wiring.length}) must be > 0"
    )

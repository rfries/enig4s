package org.somecode.enig4s
package mach

opaque type RingSetting <: Int = Int
object RingSetting:

  val zero: RingSetting = unsafe(0)
  val one: RingSetting = unsafe(1)

  def apply(c: Int): Either[String, RingSetting] =
    Either.cond(c >= 0, c, s"RingSetting ($c) must be >= 0.")

  def unsafe(n: Int): RingSetting = apply(n).fold(
    s => throw new IllegalArgumentException(s),
    v => v)

package org.somecode.enig4s.mach

opaque type Position <: Int = Int

object Position:

  val zero: Position = unsafe(0)
  val one: Position = unsafe(1)

  def apply(c: Int): Either[String, Position] =
    Either.cond(c >= 0, c, s"RingSetting ($c) must be >= 0.")

  def unsafe(n: Int): Position = apply(n).fold(
    s => throw new IllegalArgumentException(s),
    v => v)

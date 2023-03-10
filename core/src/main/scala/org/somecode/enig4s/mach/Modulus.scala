package org.somecode.enig4s.mach

import cats.kernel.Eq
import cats.data.NonEmptyVector

/**
  * Represents the modulus, or the value at which increasing values wrap
  * around to zero. This, in effect, defines the codomain of the transposition
  * functions of the various components in the [[Machine]], and of the modular
  * operations they are built on.
  */
opaque type Modulus = Int
object Modulus:

  def apply(n: Int): Either[String, Modulus] =
    if n <= 0 then
      Left(s"Modulus ($n) must be greater than zero.")
    else
      Right(n)

  def unsafe(n: Int): Modulus = n match
    case n if n <= 0 => throw new IllegalArgumentException("Modulus must be positive.")
    case n => n

  def normalize(n: Int, mod: Modulus): Int =
    val res = n % mod.toInt
    val out = if res < 0 then res + mod.toInt else res
    Modulus.unsafe(out)

  extension (mod: Modulus)
    def toInt: Int = mod
    def sum(gs: Glyph*) = normalize(gs.map(_.toInt).sum, mod)
    def plus(a: Glyph, b: Glyph) = normalize(a.toInt + b.toInt, mod)
    def minus(a: Glyph, b: Glyph) = normalize(a.toInt - b.toInt, mod)

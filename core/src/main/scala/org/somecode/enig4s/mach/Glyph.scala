package org.somecode.enig4s
package mach

/**
  * A non-negative integer value that represents a symbol being processed
  * by a [[Machine]].  A Glyph is a value between zero and the bus/wheel size
  * of the machine, which represents an offset into the symbol table in use.
  */
opaque type Glyph = Int
object Glyph:

  val zero = unsafe(0)
  val one = unsafe(1)

  def apply(value: Int): Either[String, Glyph] =
    if value < 0 then
      Left(s"Glyph ($value) must be >= 0.")
    else
      Right(value)

  def unsafe(codepoint: Int): Glyph = codepoint match
    case n if n < 0 => throw new IllegalArgumentException("Glyph can not be negative.")
    case n => n

  def normalMod(n: Int)(using mod: Modulus): Int =
    val res = n % mod.toInt
    val out = if res < 0 then res + mod.toInt else res
    Glyph.unsafe(out)

  extension (g: Glyph)
    def toInt: Int = g
    def %+(o: Glyph)(using Modulus): Glyph = normalMod(g + o.toInt)
    def %-(o: Glyph)(using Modulus): Glyph = normalMod(g - o.toInt)
    def next(using Modulus): Glyph = normalMod(g + 1)
    inline def validFor(size: Int): Boolean = size > 0 && g < size
    inline def invalidFor(size: Int): Boolean = !validFor(size)

end Glyph

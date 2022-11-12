package org.somecode.enig4s.mach

import scala.collection.immutable.ArraySeq
import cats.kernel.Eq
import cats.implicits.*

opaque type Glyph = Int
object Glyph:

  def apply(codePoint: Int)(using mod: Modulus): Either[String, Glyph] =
    if codePoint < 0 || codePoint >= mod.toInt then
      Left(s"Glyph ($codePoint) must be >= 0 and < ${mod.toInt} for this Machine instance.")
    else
      Right(codePoint)

  def unsafe(codepoint: Int): Glyph = codepoint match
    case n if n < 0 => throw new IllegalArgumentException("Glyph can not be negative.")
    case n => n

  given Eq[Glyph] = summon[Eq[Int]]
  given Ordering[Glyph] = summon[Ordering[Int]]

  def normalMod(n: Int)(using mod: Modulus): Int =
    val res = n % mod.toInt
    val out = if res < 0 then res + mod.toInt else res
    Glyph.unsafe(out)

  /**
    * Convert a sequence of glyphs into a string, via the symbol map.
    *
    * @param gs A sequence of [[Glyph]]s.  In practice, this will usually be an ArraySeq,
    *           but we convert if not so we can get a typeclass for the traverse.
    * @return The converted string.  If any invalid glyphs are found, the string "<invalid>" is returned.
    */
  def display(gs: IndexedSeq[Glyph], symbols: SymbolMap): String =
    gs.to(ArraySeq).traverse(symbols.codePoints.lift) match
      case Some(ints) => String(ints.toArray, 0, ints.length)
      case _ => "<invalid>"

  extension (g: Glyph)
    def toInt: Int = g
    def +%(o: Glyph)(using Modulus): Glyph = Glyph.normalMod(g + o.toInt)
    def -%(o: Glyph)(using Modulus): Glyph = Glyph.normalMod(g - o.toInt)

end Glyph
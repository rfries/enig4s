package org.somecode.enig4s
package mach

import cats.*
import cats.implicits.*

import scala.collection.immutable.ArraySeq
import scala.jdk.StreamConverters.*


/**
 * Convert external representations (Unicode Strings and code points)
 * to and from internal representations (indices in translation table).
 * This represents a mapping from Unicode code points to the character
 * set of the [[Machine]], represented by the offset of the code point
 * in the translation table.  Lookups will fail (return Left) for code
 * points which are not present in the translation table.
 */
final class SymbolMap private(val codePoints: IndexedSeq[Int]):

  val size: Int = codePoints.size

  private val glyphMap: Map[Int, Glyph] = codePoints.zipWithIndex.map((cp, idx) => (cp, Glyph.unsafe(idx))).toMap

  def glyphsToString(glyphs: Seq[Glyph]): Either[String,String] =
    glyphs.to(ArraySeq)
      .traverse(glyphToPoint)
      .map(a => String(a.toArray[Int], 0, a.length))

  def glyphToString(glyph: Glyph): Either[String,String] = glyphsToString(ArraySeq(glyph))

  def glyphToPoint(g: Glyph): Either[String, Int] =
    codePoints.lift(g.toInt).toRight(s"Glyph $g not found in map.")

  def stringToGlyphs(in: String): Either[String, ArraySeq[Glyph]] =
    // note that we should be calling codePoints here, but that doesn't play well with scala.js right now
    in.toArray.to(ArraySeq).traverse(ch => glyphMap.get(ch)) match
      case Some(out) => Right(out)
      case None =>
        val bad = in.filterNot(codePoints.isDefinedAt).map(c => f"'$c%c' (${c.toInt}%#04x)").mkString(",")
        Left(s"Invalid character(s) for symbol map: $bad")

  // there should be a way to do this without actually mapping, but I haven't found it yet
  def stringToInts(in: String): Either[String, ArraySeq[Int]] = stringToGlyphs(in).map(_.map(_.toInt))

  def pointToGlyph(point: Int): Either[String,Glyph] =
    glyphMap.get(point).toRight(s"Code point $point not found in symbol map.")

  /**
   * Format a glyph for display with character and numeric code. Note external
   * numeric codes are 1-based to correspond with numeric Enigma wheel labeling.
   *
   * @param in
   * @return A string with both the character and numeric representation of the glyph
   */
  def displayGlyph(in: Glyph): String =
    f"${glyphToPoint(in).map(Character.toString).getOrElse("?")} (${in.toInt + 1}%02d)"

   /**
    * Convert a sequence of glyphs into a string, via the symbol map.
    *
    * @param gs A sequence of [[Glyph]]s.  In practice, this will usually be an ArraySeq,
    *           but we convert if not so we can get a typeclass for the traverse.
    * @return The converted string.  If any invalid glyphs are found, the string "<invalid>" is returned.
    */
  def displayGlyphs(gs: IndexedSeq[Glyph]): String =
    gs.to(ArraySeq).traverse(g => codePoints.lift(g.toInt)) match
      case Some(ints) => String(ints.toArray, 0, gs.length)
      case _ => "<invalid>"

end SymbolMap

object SymbolMap:

  def apply(codes: IndexedSeq[Int]): Either[String, SymbolMap] =
    if codes.exists(_ < 0) then
      Left("Code points must be positive.")
    else if codes.length != codes.distinct.length then
      Left("Symbol maps must not contain duplicate values.")
    else
      Right(new SymbolMap(codes))

  def apply(mapping: String): Either[String, SymbolMap] =
    // all code points are valid KeyCodes, so unsafe is relatively safe here
    // note that we should be calling codePoints here, but that doesn't play well with scala.js right now
    apply(mapping.toArray.map(_.toInt).to(ArraySeq))

  val AZ: SymbolMap = SymbolMap("ABCDEFGHIJKLMNOPQRSTUVWXYZ")
    .getOrElse(throw RuntimeException("Symbol Map: Bad Init"))


end SymbolMap

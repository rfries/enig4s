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

  val glyphMap: Map[Int, Glyph] = codePoints.zipWithIndex.map((cp, idx) => (cp, Glyph.unsafe(idx))).toMap

  def glyphsToString(glyphs: Seq[Glyph]): Either[String,String] =
    glyphs.to(ArraySeq)
      .traverse(glyphToPoint)
      .map(a => String(a.toArray[Int], 0, a.length))

  def glyphToString(glyph: Glyph): Either[String,String] = glyphsToString(ArraySeq(glyph))

  def stringToGlyphs(in: String): Either[String, ArraySeq[Glyph]] =
    // note that we should be calling codePoints here, but that doesn't play well with scala.js right now
    in.toArray.to(ArraySeq).traverse(ch => glyphMap.get(ch)) match
      case Some(out) => Right(out)
      case None =>
        val bad = in.filterNot(codePoints.isDefinedAt).map(c => f"'$c%c' (${c.toInt}%#04x)").mkString(",")
        Left(s"Invalid character(s) for symbol map: $bad")

  def stringToInts(in: String): Either[String, ArraySeq[Int]] = stringToGlyphs(in).map(_.map(_.toInt))

  def glyphToPoint(g: Glyph): Either[String, Int] =
    codePoints.lift(g.toInt).toRight(s"Glyph $g not found in map.")

  def pointToGlyph(point: Int): Either[String,Glyph] =
    glyphMap.get(point).toRight(s"Code point $point not found in symbol map.")


  def displayCode(in: Glyph): String = f"${glyphToPoint(in).map(Character.toString).getOrElse("?")} ($in%02d)"

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

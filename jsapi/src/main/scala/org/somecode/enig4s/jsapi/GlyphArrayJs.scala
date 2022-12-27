package org.somecode.enig4s
package jsapi

import cats.implicits.*
import io.circe.Codec
import io.circe.generic.semiauto.*
import org.somecode.enig4s.mach.Glyph
import org.somecode.enig4s.mach.SymbolMap

import scala.collection.immutable.ArraySeq

/**
 * This class usually represents a [[Wiring]], though it is also used
 * with other entities that need a lookup table, such as [[SymbolMap]]
 * and [[MachineState]]. Each parameter specifies one of the ways to
 * specify a Wiring, and only one of them (Options) should be a Some.
 *
 * @param symbols  The symbolic representation of the map
 * @param indices  The 0-based array representation of the map
 * @param numbers  The 1-based array representation of the map
 */
case class GlyphArrayJs(symbols: Option[String], indices: Option[IndexedSeq[Int]], numbers: Option[IndexedSeq[Int]]):
  def toInts(symbols: SymbolMap): Either[String, ArraySeq[Int]] =
    this match
      case GlyphArrayJs(Some(str),  None,       None)       => symbols.stringToInts(str)
      case GlyphArrayJs(None,       Some(arr),  None)       => Right(arr.to(ArraySeq))
      case GlyphArrayJs(None,       None,       Some(arr))  => Right(arr.map(_ - 1).to(ArraySeq))
      case _ => Left("One (and only one) of 'symbols', 'numbers', or 'indices' must be specified.")

  def toGlyphs(symbols: SymbolMap): Either[String, ArraySeq[Glyph]] =
    toInts(symbols).flatMap(_.traverse(Glyph.apply))

object GlyphArrayJs:
  given Codec[GlyphArrayJs] = deriveCodec

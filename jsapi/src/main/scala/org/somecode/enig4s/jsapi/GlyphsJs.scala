package org.somecode.enig4s
package jsapi

import cats.implicits.*
import io.circe.Codec
import io.circe.generic.semiauto.*
import org.somecode.enig4s.mach.Glyph
import org.somecode.enig4s.mach.SymbolMap
import org.somecode.enig4s.mach.Wiring

import scala.collection.immutable.ArraySeq

/**
  * This class usually represents a [[Wiring]], though it is also used
  * with other entities that need a lookup table that can be specified
  * by either an array (of integers) or a symbolic string (in the context
  * of a symbol map), such as [[SymbolMap]] and [[MachineState]].
  *
  * @param symbols  The symbolic representation of the map, must be `None` if `codes` is provided.
  * @param codes    The array representation of the map, must be `None` if `symbols` is provided.
  */
case class GlyphsJs(symbols: Option[String], codes: Option[IndexedSeq[Int]]):
  def toInts(symbols: SymbolMap): Either[String, ArraySeq[Int]] =
    this match
      case GlyphsJs(Some(str), None) => symbols.stringToInts(str)
      case GlyphsJs(None, Some(arr)) => arr.to(ArraySeq).traverse(symbols.pointToGlyph).map(_.map(_.toInt))
      case _ => Left("One (and only one) of 'codes' or 'symbols' must be specified.")

  def toGlyphs(symbols: SymbolMap): Either[String, ArraySeq[Glyph]] =
    toInts(symbols).flatMap( _.traverse(Glyph.apply))

object GlyphsJs:
  given Codec[GlyphsJs] = deriveCodec

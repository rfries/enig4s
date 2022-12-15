package org.somecode.enig4s
package jsapi

import io.circe.Codec
import io.circe.generic.semiauto.*
import org.somecode.enig4s.mach.{SymbolMap, Glyph}

case class GlyphJs(symbol: Option[String], code: Option[Int]):
  def toGlyph(symbols: SymbolMap): Either[String, Glyph] = this match
    case GlyphJs(Some(str), None) => str match
      case s if s.length != 1 => Left("'symbol' length must be 1")
      case s => symbols.pointToGlyph(str(0))
    case GlyphJs(None, Some(n)) => Glyph(n)
    case _ => Left("One (and only one) of 'code' or 'symbol' must be specified.")

object GlyphJs:
  given Codec[GlyphJs] = deriveCodec

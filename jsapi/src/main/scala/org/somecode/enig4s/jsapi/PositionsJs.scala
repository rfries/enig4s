package org.somecode.enig4s
package jsapi

import cats.implicits.*
import io.circe.Codec
import io.circe.generic.semiauto.*
import org.somecode.enig4s.mach.{Glyph, SymbolMap}

import scala.collection.immutable.ArraySeq

case class PositionsJs(symbols: Option[String], codes: Option[ArraySeq[Int]]):
  def toPositions(symbols: SymbolMap): Either[String, ArraySeq[Glyph]] = this match
    case PositionsJs(Some(str), None) => symbols.stringToGlyphs(str)
    case PositionsJs(None, Some(arr)) => arr.traverse(Glyph.apply)
    case _ => Left("One (and only one) of 'codes' or 'symbols' must be specified.")

object PositionsJs:
  given Codec[PositionsJs] = deriveCodec

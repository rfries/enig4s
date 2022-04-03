package org.somecode.enig4s
package jsapi

import cats.implicits.*
import io.circe.Codec
import io.circe.generic.semiauto.*
import org.somecode.enig4s.mach.{Position, SymbolMap}

import scala.collection.immutable.ArraySeq

case class PositionsJs(symbols: Option[String], codes: Option[ArraySeq[Int]]):
  def toPositions(symbols: SymbolMap): Either[String, ArraySeq[Position]] = this match
    // TODO:                                           vv - not OK, need to fix SymbolMap
    case PositionsJs(Some(str), None) => symbols.stringToCodes(str).map(_.map(Position.unsafe))
    case PositionsJs(None, Some(arr)) => arr.map(Position.apply).sequence
    case _ => Left("One (and only one) of 'codes' or 'symbols' must be specified.")

object PositionsJs:
  given Codec[PositionsJs] = deriveCodec

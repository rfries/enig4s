package org.somecode.enig4s
package jsapi

import cats.implicits.*
import io.circe.Codec
import io.circe.generic.semiauto.*
import org.somecode.enig4s.mach.{KeyCode, SymbolMap}

import scala.collection.immutable.ArraySeq

case class CodesJs(symbols: Option[String], codes: Option[ArraySeq[Int]]):
  def toCodes(symbols: SymbolMap): Either[String, ArraySeq[KeyCode]] = this match
    case CodesJs(Some(str), None) => symbols.stringToCodes(str)
    case CodesJs(None, Some(arr)) => arr.map(KeyCode.apply).sequence
    case _ => Left("One (and only one) of 'codes' or 'symbols' must be specified.")

object CodesJs:
  given Codec[CodesJs] = deriveCodec

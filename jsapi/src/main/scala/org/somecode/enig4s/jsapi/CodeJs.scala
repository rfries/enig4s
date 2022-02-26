package org.somecode.enig4s
package jsapi

import io.circe.Codec
import io.circe.generic.semiauto.*
import org.somecode.enig4s.mach.{SymbolMap, KeyCode}

case class CodeJs(symbols: Option[String], codes: Option[Int]):
  def toCodes(symbols: SymbolMap): Either[String, KeyCode] = this match
    case CodeJs(Some(str), None) => str match
      case s if s.length != 1 => Left("'symbol' length must be 1")
      case s => symbols.pointToCode(str(0))
    //Right(str.map(KeyCode.unsafe(_)).toVector)
    case CodeJs(None, Some(n)) => KeyCode(n)
    case _ => Left("One (and only one) of 'codes' or 'symbols' must be specified.")

object CodeJs:
  given Codec[CodeJs] = deriveCodec

package org.somecode.enig4s
package jsapi

import cats.implicits.*
import io.circe.Codec
import io.circe.generic.semiauto.*
import org.somecode.enig4s.mach.{SymbolMap, KeyCode}

import scala.collection.immutable.ArraySeq

case class CodeJs(symbol: Option[String], code: Option[Int]):
  def toCode(symbols: SymbolMap): Either[String, KeyCode] = this match
    case CodeJs(Some(str), None) => str match
      case s if s.length != 1 => Left("'symbol' length must be 1")
      case s => symbols.pointToCode(str(0))
    case CodeJs(None, Some(n)) => KeyCode(n)
    case _ => Left("One (and only one) of 'code' or 'symbol' must be specified.")

object CodeJs:
  given Codec[CodeJs] = deriveCodec

package org.somecode.enig4s
package jsapi

import cats.implicits.*
import io.circe.Codec
import io.circe.generic.semiauto.*
import org.somecode.enig4s.mach.{EnigmaPlugBoard, KeyCode, PlugBoard, SymbolMap}

import scala.collection.immutable.ArraySeq

final case class PlugsJs(symbols: Option[ArraySeq[String]], codes: Option[ArraySeq[ArraySeq[Int]]]):
  def toPlugBoard(symbols: SymbolMap, size: Int): Either[String, PlugBoard] = this match
    case PlugsJs(Some(pairs), None) => EnigmaPlugBoard(size, pairs, symbols)
    case PlugsJs(None, Some(cds)) =>
      if codes.exists(_.length == 2) then
        Left(s"All elements of plugboard array must be pairs (length 2)")
      else
        val kc: ArraySeq[Either[String, ArraySeq[KeyCode]]] =
          cds.map(_.map(KeyCode.apply).sequence)

        val tuples: Either[String, ArraySeq[(KeyCode, KeyCode)]] =
          kc.map(_.map(k => k(0) -> k(1))).sequence

        tuples.flatMap(pairs => EnigmaPlugBoard(size, pairs))
    case _ => Left("One (and only one) of 'codes' or 'symbols' must be specified for plugboard settings.")

object PlugsJs:
  given Codec[PlugsJs] = deriveCodec

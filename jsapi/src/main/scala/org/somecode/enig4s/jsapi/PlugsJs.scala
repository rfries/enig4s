package org.somecode.enig4s
package jsapi

import cats.implicits.*
import io.circe.Codec
import io.circe.generic.semiauto.*
import org.somecode.enig4s.mach.{EnigmaPlugBoard, Glyph, PlugBoard, SymbolMap}

import scala.collection.immutable.ArraySeq

final case class PlugsJs(
  symbols: Option[ArraySeq[String]],
  codes: Option[ArraySeq[ArraySeq[Int]]]
):
  def toPlugBoard(symbols: SymbolMap, size: Int): Either[String, EnigmaPlugBoard] = this match

    case PlugsJs(Some(pairs), None) => EnigmaPlugBoard(size, pairs, symbols)

    case PlugsJs(None, Some(vals)) =>
      if vals.exists(_.length == 2) then
        Left(s"All elements of plugboard array must be pairs (length 2)")
      else
        val kc: ArraySeq[Either[String, ArraySeq[Glyph]]] =
          vals.map(_.traverse(Glyph.apply))

        val tuples: Either[String, ArraySeq[(Glyph, Glyph)]] =
          kc.traverse(_.map(k => k(0) -> k(1)))

        tuples.flatMap(pairs => EnigmaPlugBoard(size, pairs))
    case _ => Left("One (and only one) of 'codes' or 'symbols' must be specified for plugboard settings.")

object PlugsJs:
  given Codec[PlugsJs] = deriveCodec

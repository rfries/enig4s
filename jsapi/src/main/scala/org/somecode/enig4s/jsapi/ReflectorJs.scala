package org.somecode.enig4s
package jsapi

import cats.implicits.*
import io.circe.Codec
import io.circe.generic.semiauto.*
import org.somecode.enig4s.mach.{Cabinet, Glyph, Reflector, SymbolMap, Wiring}

import scala.collection.immutable.ArraySeq

final case class ReflectorJs(
    name: Option[String],
    wiring: Option[GlyphArrayJs]
):
  def toReflector(symbols: SymbolMap, cabinet: Cabinet): Either[String, Reflector] =
    this match
      case ReflectorJs(Some(nm), None) =>
        cabinet.findReflector(nm).toRight(s"Reflector '$nm' not found")

      case ReflectorJs(None, Some(wiresJs)) =>
        for
          wireCodes <- wiresJs.toGlyphs(symbols)
          wires <- Wiring.apply(wireCodes)
          ref <- Reflector(wires)
        yield ref

      case _ => Left("One of 'name' or 'wiring' must be provided for a reflector")

object ReflectorJs:
  given Codec[ReflectorJs] = deriveCodec

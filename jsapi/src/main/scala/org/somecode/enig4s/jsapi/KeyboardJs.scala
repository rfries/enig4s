package org.somecode.enig4s.jsapi

import cats.implicits.*
import io.circe.Codec
import io.circe.generic.semiauto.*
import org.somecode.enig4s.mach.{Cabinet, SymbolMap, Wiring}

final case class KeyboardJs(name: Option[String], wiring: Option[GlyphArrayJs]):
  def toWiring(symbols: SymbolMap, cabinet: Cabinet): Either[String, Wiring] =
    this match
      case KeyboardJs(Some(nm), None) => cabinet.findWiring(nm).toRight(s"Wiring name '$nm' not found'")
      case KeyboardJs(None, Some(wires)) => wires.toGlyphs(symbols).flatMap(Wiring.apply)
      case _ => Left("One of 'name' or 'wiring' (but not both) must be specified")

object KeyboardJs:
  given Codec[KeyboardJs] = deriveCodec

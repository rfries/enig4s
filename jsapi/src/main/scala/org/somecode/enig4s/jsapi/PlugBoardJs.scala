package org.somecode.enig4s.jsapi

import io.circe.Codec
import io.circe.generic.semiauto.*
import org.somecode.enig4s.mach.EnigmaPlugBoard
import org.somecode.enig4s.mach.SymbolMap
import org.somecode.enig4s.mach.Wiring
import org.somecode.enig4s.mach.PlugBoard

final case class PlugBoardJs(
  wiring: Option[GlyphsJs],
  plugs: Option[PlugsJs]
):
  def toPlugBoard(size: Int, symbols: SymbolMap): Either[String, EnigmaPlugBoard] = this match
    case PlugBoardJs(Some(glyphsJs), None) =>
      Left("TypeX plugboard not yet supported")
      // for
      //   glyphs <- glyphsJs.toGlyphs(symbols)
      //   wiring <- Wiring(glyphs)
      //   pb <- TypeXPlugBoard(size, wiring)
      // yield pb

    case PlugBoardJs(None, Some(pl)) => pl.toPlugBoard(symbols, size)

    case _ => Left("Exactly one of 'wiring' or 'plugs' must be defined")

object PlugBoardJs:
  given Codec[PlugBoardJs] = deriveCodec
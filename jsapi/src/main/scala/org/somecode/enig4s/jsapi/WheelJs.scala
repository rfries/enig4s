package org.somecode.enig4s
package jsapi

import cats.implicits.*
import io.circe.Codec
import io.circe.generic.semiauto.*
import org.somecode.enig4s.mach.{Cabinet, Glyph, SymbolMap, Wheel, Wiring}

final case class WheelJs(
                          name: Option[String],
                          wiring: Option[GlyphArrayJs],
                          notches: Option[GlyphArrayJs]
):
  def toWheel(symbols: SymbolMap, cabinet: Cabinet): Either[String, Wheel] = this match
    case WheelJs(Some(name), None, None) => cabinet.findWheel(name).toRight(s"Wheel '$name' not found'")
    case WheelJs(None, Some(wires), Some(notch)) =>
      for
        glyphs <- wires.toGlyphs(symbols)
        wr <- Wiring(glyphs)
        nt <- notch.toGlyphs(symbols)
        wh <- Wheel(wr, nt, 0)
      yield wh
    case _ => Left(s"Wheels must contain either 'name' or 'mapping' and 'notches' only.")

object WheelJs:
  given Codec[WheelJs] = deriveCodec[WheelJs]

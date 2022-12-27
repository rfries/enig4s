package org.somecode.enig4s
package jsapi

import cats.implicits.*
import io.circe.Codec
import io.circe.generic.semiauto.*
import org.somecode.enig4s.mach.{Glyph, MachineState, RingSetting, SymbolMap, WheelState}
import scala.collection.immutable.ArraySeq

case class SettingsJs private (
                                rings: GlyphArrayJs,
                                wheels: GlyphArrayJs,
                                reflector: Option[GlyphJs],
                                plugboard: Option[PlugBoardJs]
):
  def toMachineState(symbols: SymbolMap, numWheels: Int, busSize: Int): Either[String, MachineState] =
    for
      ringGlyphs <- rings.toGlyphs(symbols).map(_.reverse)
      posGlyphs <- wheels.toGlyphs(symbols).map(_.reverse)
      _ <- Either.cond(ringGlyphs.size === posGlyphs.size, (), "Position and ring settings strings must be the same length")
      pb <- plugboard.traverse(pb => pb.toPlugBoard(busSize, symbols))

      ref <- reflector
              .map( _.toGlyph(symbols))
              .getOrElse(Right(Glyph.zero))
    yield
      MachineState(posGlyphs, ringGlyphs, ref, pb, symbols)

object SettingsJs:
  given Codec[SettingsJs] = deriveCodec

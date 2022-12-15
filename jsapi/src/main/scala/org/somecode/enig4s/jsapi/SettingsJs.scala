package org.somecode.enig4s
package jsapi

import cats.implicits.*
import io.circe.Codec
import io.circe.generic.semiauto.*
import org.somecode.enig4s.mach.{Glyph, MachineState, RingSetting, SymbolMap, WheelState}
import scala.collection.immutable.ArraySeq

case class SettingsJs private (
  rings: GlyphsJs,
  wheels: GlyphsJs,
  reflector: Option[GlyphJs]
):
  def toMachineState(symbols: SymbolMap, numWheels: Int, busSize: Int): Either[String, MachineState] =
    for
      ringCodes <- rings.toGlyphs(symbols).map(_.reverse)
      posGlyphs <- wheels.toGlyphs(symbols).map(_.reverse)
      //_ <- Either.cond(ringCodes.size === posCodes.size, (), "Position and ring settings strings must be the same length")
      //ws = posGlyphs

      ref <- reflector
              .map( _.toGlyph(symbols))
              .getOrElse(Right(Glyph.zero))

    yield MachineState(posGlyphs, ref, symbols)

object SettingsJs:
  given Codec[SettingsJs] = deriveCodec

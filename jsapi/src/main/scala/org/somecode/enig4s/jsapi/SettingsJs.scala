package org.somecode.enig4s
package jsapi

import cats.implicits.*
import io.circe.Codec
import io.circe.generic.semiauto.*
import org.somecode.enig4s.mach.{Glyph, MachineState, SymbolMap}
import scala.collection.immutable.ArraySeq

case class SettingsJs private (
    rings: GlyphArrayJs,
    positions: GlyphArrayJs,
    reflector: Option[GlyphJs],
    plugboard: Option[PlugBoardJs],
    trace: Option[Boolean]
):
  def toMachineState(symbols: SymbolMap, numWheels: Int, busSize: Int): Either[String, MachineState] =
    for
      ringGlyphs <- rings.toGlyphs(symbols).map(_.reverse)
      posGlyphs <- positions.toGlyphs(symbols).map(_.reverse)
      _ <- Either.cond(ringGlyphs.size === posGlyphs.size, (), "Position and ring settings strings must be the same length")
      pb <- plugboard.traverse(pb => pb.toPlugBoard(busSize, symbols))

      ref <- reflector
              .map( _.toGlyph(symbols))
              .getOrElse(Right(Glyph.zero))
      mstate = MachineState(posGlyphs, ringGlyphs, ref, pb, symbols)
    yield
      if trace.getOrElse(false) then mstate.withTrace
      else mstate

object SettingsJs:
  given Codec[SettingsJs] = deriveCodec

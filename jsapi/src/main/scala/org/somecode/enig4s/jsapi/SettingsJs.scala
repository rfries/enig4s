package org.somecode.enig4s
package jsapi

import cats.implicits.*
import io.circe.Codec
import io.circe.generic.semiauto.*
import org.somecode.enig4s.mach.{MachineState, Position, SymbolMap}

case class SettingsJs private (
  rings: CodesJs,
  wheels: CodesJs,
  reflector: Option[CodeJs],
  plugs: PlugsJs
):
  def toMachineState(symbols: SymbolMap, numWheels: Int, busSize: Int): Either[String, MachineState] =
    for
      rs <- rings.toCodes(symbols)
      wh <- wheels.toCodes(symbols)
      ref <- reflector.map(_.toCodes(symbols)).getOrElse(Right(Vector(Position.zero)))
      //plg <- plugs.toPlugBoard(symbols)
    yield ???


object SettingsJs:
  given Codec[SettingsJs] = deriveCodec

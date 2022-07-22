package org.somecode.enig4s
package jsapi

import cats.implicits.*
import io.circe.Codec
import io.circe.generic.semiauto.*
import org.somecode.enig4s.mach.{KeyCode, MachineState, Position, RingSetting, SymbolMap, WheelState}

case class SettingsJs private (
  rings: CodesJs,
  wheels: CodesJs,
  reflector: Option[CodeJs]
):
  def toMachineState(symbols: SymbolMap, numWheels: Int, busSize: Int): Either[String, MachineState] =
    for
      ringCodes <- rings.toCodes(symbols).map(_.reverse)
      posCodes <- wheels.toCodes(symbols).map(_.reverse)
      _ <- Either.cond(ringCodes.size === posCodes.size, (), "Position and ring settings strings must be the same length")
      ws = ringCodes.zip(posCodes)
              .map((ring, pos) => WheelState(pos, RingSetting.unsafe(ring)))

      ref <- reflector
              .map( _.toCode(symbols).map(Position.unsafe))
              .getOrElse(Right(Position.zero))

    yield MachineState(ws, ref)

object SettingsJs:
  given Codec[SettingsJs] = deriveCodec

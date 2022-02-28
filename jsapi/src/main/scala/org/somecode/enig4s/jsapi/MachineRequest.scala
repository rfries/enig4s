package org.somecode.enig4s
package jsapi

import io.circe.Codec
import io.circe.generic.semiauto.*
import org.somecode.enig4s.mach.{Cabinet, Machine, SymbolMap, Wiring}

final case class MachineRequest(
  maybeSymbolMap: Option[SymbolMapJs],
  maybeKeyboard: Option[WiringJs],
  maybeWheels: Vector[WheelJs],
  reflector: WiringJs,
  settings: SettingsJs,
  maybeText: Option[String]
):
  def toMachine(cabinet: Cabinet): Either[String, Machine] =
    for
      smap <- maybeSymbolMap.map(_.toSymbolMap(cabinet)) match
        case Some(sm) => sm
        case None => Right(SymbolMap.AZ)

      kb <- maybeKeyboard.map(_.toWiring(smap, cabinet)) match
        case Some(k) => k
        case None => Right(Wiring.AZ)

      //wh <- maybeWheels.map(_.)

    yield ???
object MachineRequest:
  given Codec[MachineRequest] = deriveCodec[MachineRequest]

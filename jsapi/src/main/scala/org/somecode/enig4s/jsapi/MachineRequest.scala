package org.somecode.enig4s
package jsapi

import cats.implicits.*

import io.circe.Codec
import io.circe.generic.semiauto.*
import org.somecode.enig4s.mach.{Cabinet, Machine, SymbolMap, Wiring}

final case class MachineRequest(
  busSize: Option[Int],
  symbolMap: Option[SymbolMapJs],
  keyboard: Option[KeyboardJs],
  wheels: Vector[WheelJs],
  reflector: ReflectorJs,
  settings: SettingsJs,
  text: Option[String]
):
  def toMachine(cabinet: Cabinet): Either[String, Machine] =
    for
      smap <- symbolMap.map(_.toSymbolMap(cabinet)) match
        case Some(sm) => sm
        case None => Right(SymbolMap.AZ)

      kb <- keyboard.map(_.toWiring(smap, cabinet)) match
        case Some(k) => k
        case None => Right(Wiring.AZ)

      wh <- wheels.map(_.toWheel(smap, cabinet)).sequence

      rf <- reflector.toReflector(smap, cabinet)

      //set <- settings

      mach <- Machine(smap, kb, wh, rf, ???)
    yield mach

object MachineRequest:
  given Codec[MachineRequest] = deriveCodec[MachineRequest]

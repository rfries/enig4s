package org.somecode.enig4s
package jsapi

import cats.implicits.*
import io.circe.Codec
import io.circe.generic.semiauto.*
import org.somecode.enig4s.mach.*

final case class MachineRequestJs(
  symbolMap: Option[SymbolMapJs],
  keyboard: Option[KeyboardJs],
  wheels: Vector[WheelJs],
  reflector: ReflectorJs,
  plugboard: Option[PlugBoardJs],
  settings: SettingsJs,
  text: String
):
  def toMachineRequest(cabinet: Cabinet): Either[String, MachineRequest] =
    for
      smap    <- symbolMap.map(_.toSymbolMap(cabinet)) match
        case Some(sm) => sm
        case None => Right(SymbolMap.AZ)

      kbwires <- keyboard.map(_.toWiring(smap, cabinet)) match
          case Some(k) => k
          case None => Wiring.passthrough(26)
      
      kb = Entry(kbwires)
      wh      <- wheels.traverse(_.toWheel(smap, cabinet))
      rf      <- reflector.toReflector(smap, cabinet)
      pb      <- plugboard.traverse(pb => pb.toPlugBoard(kb.length, smap))
      machine <- Machine(kb, wh, rf, pb, smap)
      mstate  <- settings.toMachineState(smap, wh.size, kb.length)

    yield MachineRequest(machine, mstate, text)

object MachineRequestJs:
  given Codec[MachineRequestJs] = deriveCodec[MachineRequestJs]

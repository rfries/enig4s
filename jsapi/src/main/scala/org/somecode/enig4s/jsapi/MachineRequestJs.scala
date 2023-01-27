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
  settings: SettingsJs,
  text: String,
  trace: Option[Boolean]
):
  def toMachineRequest(cabinet: Cabinet): Either[String, MachineRequest] =
    for
      symbols <- symbolMap.map(_.toSymbolMap(cabinet)) match
        case Some(sm) => sm
        case None => Right(SymbolMap.AZ)

      kbwires <- keyboard.map(_.toWiring(symbols, cabinet)) match
        case Some(k) => k
        case None => Wiring.passthrough(symbols.size)

      entry   = Entry(kbwires)
      uncfg   <- wheels.reverse.traverse(_.toWheel(symbols, cabinet))
      wh      <- uncfg.zipWithIndex.traverse((wheel, i) => wheel.copy(wheelNum = i))
      rf      <- reflector.toReflector(symbols, cabinet)
      machine <- Machine(entry, wh, rf, symbols)
      mstate  <- settings.toMachineState(symbols, wh.size, entry.length)

    yield MachineRequest(machine, mstate, text)

object MachineRequestJs:
  given Codec[MachineRequestJs] = deriveCodec[MachineRequestJs]

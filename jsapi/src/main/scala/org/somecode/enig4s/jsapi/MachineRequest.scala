package org.somecode.enig4s
package jsapi

import io.circe.Codec
import io.circe.generic.semiauto._

final case class MachineRequest(
  symbolMap: Option[SymbolMapJs],
  keyboard: Option[KeyboardJs],
  wheels: Vector[WheelJs],
  reflector: ReflectorJs,
  settings: SettingsJs,
  text: Option[String]
)

object MachineRequest:
  given Codec[MachineRequest] = deriveCodec[MachineRequest]

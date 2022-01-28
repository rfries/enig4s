package org.somecode.enig4s
package jsapi

import io.circe.Codec
import io.circe.generic.semiauto._

final case class MachineRequest(
  machineType: Option[String],
  wheels: Vector[WheelJs],
  ringSettings: String,
  wheelPositions: String,
  reflectorName: String,
  reflectorPosition: Option[String],
  plugs: Vector[String],
  text: String
)

object MachineRequest:
  given Codec[MachineRequest] = deriveCodec[MachineRequest]

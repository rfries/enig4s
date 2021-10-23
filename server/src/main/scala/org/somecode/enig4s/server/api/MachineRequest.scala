package org.somecode.enig4s.server.api

import org.somecode.enigma.mach.Wheel
import io.circe.Decoder
import io.circe.generic.semiauto._

final case class MachineRequest(
  machineType: Option[String],
  wheelNames: Vector[String],
  ringSettings: String,
  wheelPositions: String,
  reflectorName: String,
  reflectorPosition: Option[String],
  plugs: Vector[String],
  text: String
)

object MachineRequest:
  given Decoder[MachineRequest] = deriveDecoder[MachineRequest]

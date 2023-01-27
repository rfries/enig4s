package org.somecode.enig4s
package jsapi

import io.circe.generic.semiauto.*
import io.circe.{Decoder, Encoder}

final case class MachineResponse(
  text: String,
  wheelPositions: String,
  trace: Option[String]
)

object MachineResponse:
  given Encoder[MachineResponse] = deriveEncoder[MachineResponse].mapJson(_.deepDropNullValues)
  given Decoder[MachineResponse] = deriveDecoder[MachineResponse]

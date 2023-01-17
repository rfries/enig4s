package org.somecode.enig4s
package jsapi

import io.circe.generic.semiauto._
import io.circe.Codec

final case class MachineResponse(
  text: String,
  wheelPositions: String,
  trace: Option[String]
)

object MachineResponse:
  given Codec[MachineResponse] = deriveCodec[MachineResponse]

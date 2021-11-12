package org.somecode.enig4s
package jsapi

import io.circe.generic.semiauto.deriveEncoder
import io.circe.Encoder

final case class MachineResponse(
  wheelPositions: String,
  text: String
)

//object MachineResponse:
  //given Encoder[MachineRequest] = deriveEncoder[MachineRequest]

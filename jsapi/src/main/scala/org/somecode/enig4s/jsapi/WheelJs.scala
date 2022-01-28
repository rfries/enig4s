package org.somecode.enig4s
package jsapi

import io.circe.generic.semiauto._
import io.circe.Codec

final case class WheelJs(
  name: Option[String],
  mapping: Option[String],
  notches: Option[String],
  position: String,
  ringSetting: String)

object WheelJs:
  // def fromWheel(name: String, wheel: Wheel): WheelJs = WheelJs(
  //   name,
  //   wheel.wiring.
  // )
  given Codec[WheelJs] = deriveCodec[WheelJs]

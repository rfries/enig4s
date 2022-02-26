package org.somecode.enig4s
package jsapi

import cats.implicits.*
import io.circe.Codec
import io.circe.generic.semiauto.*

final case class WheelJs(
  name: Option[String],
  mapping: Option[CodesJs],
  notches: Option[CodesJs]
)

object WheelJs:
  given Codec[WheelJs] = deriveCodec[WheelJs]

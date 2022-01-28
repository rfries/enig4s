package org.somecode.enig4s.jsapi

import io.circe.Codec
import io.circe.generic.semiauto._

final case class CharacterMapJs(
  name: String,
  size: Option[Int],
  mapCodes: Option[Vector[Int]],
  map: Option[String]
)

object CharacterMapJs:
  given Codec[CharacterMapJs] = deriveCodec[CharacterMapJs]

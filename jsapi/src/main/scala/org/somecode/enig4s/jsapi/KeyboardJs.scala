package org.somecode.enig4s.jsapi

import cats.implicits.*
import io.circe.Codec
import io.circe.generic.semiauto.*

case class KeyboardJs(name: Option[String], mapping: Option[CodesJs])

object KeyboardJs:
  given Codec[KeyboardJs] = deriveCodec

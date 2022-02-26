package org.somecode.enig4s.jsapi

import cats.implicits.*
import io.circe.Codec
import io.circe.generic.semiauto.*

case class ReflectorJs(name: Option[String], mapping: Option[CodesJs])

object ReflectorJs:
  given Codec[ReflectorJs] = deriveCodec

package org.somecode.enig4s
package jsapi

import cats.implicits.*
import io.circe.Codec
import io.circe.generic.semiauto.*

case class PlugsJs(symbols: Option[Vector[String]], codes: Option[Vector[Vector[Int]]])

object PlugsJs:
  given Codec[PlugsJs] = deriveCodec

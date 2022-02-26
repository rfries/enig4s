package org.somecode.enig4s
package jsapi

import cats.implicits.*
import io.circe.Codec
import io.circe.generic.semiauto.*

case class SettingsJs(
  rings: Option[CodesJs],
  wheels: Option[CodesJs],
  reflector: Option[CodesJs],
  plugs: PlugsJs
)

object SettingsJs:
  given Codec[SettingsJs] = deriveCodec

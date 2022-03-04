package org.somecode.enig4s
package jsapi

import cats.implicits.*
import io.circe.Codec
import io.circe.generic.semiauto.*

case class SettingsJs(
  rings: CodesJs,
  wheels: CodesJs,
  reflector: Option[CodeJs],
  plugs: PlugsJs
)

object SettingsJs:
  given Codec[SettingsJs] = deriveCodec

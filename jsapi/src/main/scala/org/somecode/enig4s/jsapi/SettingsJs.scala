package org.somecode.enig4s
package jsapi

import cats.implicits.*
import io.circe.Codec
import io.circe.generic.semiauto.*
import org.somecode.enig4s.mach.MachineState

case class SettingsJs private (
  rings: CodesJs,
  wheels: CodesJs,
  reflector: Option[CodeJs],
  plugs: PlugsJs
):
  def toMachineState: Either[String, MachineState] = ???

object SettingsJs:
  given Codec[SettingsJs] = deriveCodec

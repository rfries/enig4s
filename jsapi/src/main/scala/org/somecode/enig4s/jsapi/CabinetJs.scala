package org.somecode.enig4s
package jsapi

import io.circe.Encoder
import io.circe.Json
import io.circe.generic.semiauto.deriveEncoder
import org.somecode.enig4s.mach.Cabinet

object CabinetJs:

  import Cabinet._

  given Encoder[SymbolMapInit] = deriveEncoder
  given Encoder[WiringInit] = deriveEncoder
  given Encoder[WheelInit] = deriveEncoder
  given Encoder[ReflectorInit] = deriveEncoder

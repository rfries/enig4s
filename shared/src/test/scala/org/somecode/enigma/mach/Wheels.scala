package org.somecode.enigma.mach

import cats.*
import cats.implicits.*

object Wheels:

  val I:    Wheel = Wheel(Wirings.I,    "Q").require
  val II:   Wheel = Wheel(Wirings.II,   "E").require
  val III:  Wheel = Wheel(Wirings.III,  "V").require
  val IV:   Wheel = Wheel(Wirings.IV,   "J").require
  val V:    Wheel = Wheel(Wirings.V,    "Z").require

  val VI:   Wheel = Wheel(Wirings.VI,   "ZM").require
  val VII:  Wheel = Wheel(Wirings.VII,  "ZM").require
  val VIII: Wheel = Wheel(Wirings.VIII, "ZM").require

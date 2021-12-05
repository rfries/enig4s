package org.somecode.enig4s
package mach

import cats.*
import cats.implicits.*

object Wheels:

  val I:    Wheel = Wheel(Wirings.I,    "Q",  KeyCode.zero).require
  val II:   Wheel = Wheel(Wirings.II,   "E",  KeyCode.zero).require
  val III:  Wheel = Wheel(Wirings.III,  "V",  KeyCode.zero).require
  val IV:   Wheel = Wheel(Wirings.IV,   "J",  KeyCode.zero).require
  val V:    Wheel = Wheel(Wirings.V,    "Z",  KeyCode.zero).require

  val VI:   Wheel = Wheel(Wirings.VI,   "ZM", KeyCode.zero).require
  val VII:  Wheel = Wheel(Wirings.VII,  "ZM", KeyCode.zero).require
  val VIII: Wheel = Wheel(Wirings.VIII, "ZM", KeyCode.zero).require

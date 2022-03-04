package org.somecode.enig4s
package mach

import cats.implicits.*

object Wheels:

  val symbols = SymbolMap.AZ

  val I:    Wheel = Wheel(Wirings.I,    "Q", symbols).require
  val II:   Wheel = Wheel(Wirings.II,   "E", symbols).require
  val III:  Wheel = Wheel(Wirings.III,  "V", symbols).require

  val IV:   Wheel = Wheel(Wirings.IV,   "J", symbols).require
  val V:    Wheel = Wheel(Wirings.V,    "Z", symbols).require

  val VI:   Wheel = Wheel(Wirings.VI,   "ZM", symbols).require
  val VII:  Wheel = Wheel(Wirings.VII,  "ZM", symbols).require
  val VIII: Wheel = Wheel(Wirings.VIII, "ZM", symbols).require

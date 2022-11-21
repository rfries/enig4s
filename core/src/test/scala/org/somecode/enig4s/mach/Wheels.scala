package org.somecode.enig4s
package mach

import cats.implicits.*

object Wheels:

  val symbols = SymbolMap.AZ

  val I:    Wheel = Wheel(Wirings.I,    "Q", "A", 0, symbols).require
  val II:   Wheel = Wheel(Wirings.II,   "E", "A", 0, symbols).require
  val III:  Wheel = Wheel(Wirings.III,  "V", "A", 0, symbols).require

  val IV:   Wheel = Wheel(Wirings.IV,   "J", "A", 0, symbols).require
  val V:    Wheel = Wheel(Wirings.V,    "Z", "A", 0, symbols).require

  val VI:   Wheel = Wheel(Wirings.VI,   "ZM", "A", 0, symbols).require
  val VII:  Wheel = Wheel(Wirings.VII,  "ZM", "A", 0, symbols).require
  val VIII: Wheel = Wheel(Wirings.VIII, "ZM", "A", 0, symbols).require

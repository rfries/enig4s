package org.somecode.enigma.mach

import cats.*
import cats.implicits.*

object Wheels:

  val I:    Wheel = Wheel(Wirings.I,    Notches(Set("Q")).require).require
  val II:   Wheel = Wheel(Wirings.II,   Notches(Set("E")).require).require
  val III:  Wheel = Wheel(Wirings.III,  Notches(Set("V")).require).require
  val IV:   Wheel = Wheel(Wirings.IV,   Notches(Set("J")).require).require
  val V:    Wheel = Wheel(Wirings.V,    Notches(Set("Z")).require).require

  val VI:   Wheel = Wheel(Wirings.VI,   Notches(Set("Z", "M")).require).require
  val VII:  Wheel = Wheel(Wirings.VII,  Notches(Set("Z", "M")).require).require
  val VIII: Wheel = Wheel(Wirings.VIII, Notches(Set("Z", "M")).require).require

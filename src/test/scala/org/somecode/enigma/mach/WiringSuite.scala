package org.somecode.enigma
package mach

import munit.FunSuite

class WiringSuite extends FunSuite:
  test("Wiring creation") {
    assert(Wiring.fromString("ABCDEFGHIJKLMNOPQRSTUVWXYZ").isInstanceOf[Right[String, Wiring]])
    assert(Wiring.fromString("ZABCDEFGHIJKLMNOPQRSTUVWXY").isInstanceOf[Right[String, Wiring]])

    assert(Wiring.fromString("ABBDEFGHIJKLMNOPQRSTUVWXYZ").isInstanceOf[Left[String, Wiring]])
    assert(Wiring.fromString("ZABCDEFGHIJKLMNOPQRSTUVWXY12345").isInstanceOf[Left[String, Wiring]])
    assert(Wiring.fromString("ABCDEFGHIJKL-MNOPQRSTUVWXY").isInstanceOf[Left[String, Wiring]])
    assert(Wiring.fromString("").isInstanceOf[Left[String, Wiring]])
  }

  test("Wiring translation") {
    val p0 = Position.zero
    val p1 = Position(1).require

    val wiringId = Wiring.fromString("ABCDEFGHIJKLMNOPQRSTUVWXYZ").require
    assert(wiringId.forward(p0.value) == p0)
    assert(wiringId.reverse(p0.value) == p0)
    val wiringPlusOne = Wiring.fromString("BCDEFGHIJKLMNOPQRSTUVWXYZA").require
    assert(wiringPlusOne.forward(p0.value) == p1)
    assert(wiringPlusOne.reverse(p1.value) == p0)
  }

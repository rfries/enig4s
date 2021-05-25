package org.somecode.enigma
package mach

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.EitherValues.*
import org.scalatest.matchers.should
import scala.util.Left

class WiringSpec extends AnyFunSuite with should.Matchers {

  test("Wiring creation") {
    Wiring.fromString("ABCDEFGHIJKLMNOPQRSTUVWXYZ").value shouldBe a [Wiring]
    Wiring.fromString("ABCDEFGHIJKLMNOPQRSTUVWXYZ").value shouldBe a [Wiring]
    Wiring.fromString("ZABCDEFGHIJKLMNOPQRSTUVWXY").value shouldBe a [Wiring]

    assert(Wiring.fromString("ABBDEFGHIJKLMNOPQRSTUVWXYZ").isLeft)
    assert(Wiring.fromString("ZABCDEFGHIJKLMNOPQRSTUVWXY12345").isLeft)
    assert(Wiring.fromString("ABCDEFGHIJKL-MNOPQRSTUVWXY").isLeft)
    assert(Wiring.fromString("").isLeft)
  }

  test("Wiring translation") {
    val p0 = Position.zero
    val p1 = Position(1).require

    val wiringId = Wiring.fromString("ABCDEFGHIJKLMNOPQRSTUVWXYZ").require
    assert(wiringId.forward(p0.value) === p0)
    assert(wiringId.reverse(p0.value) === p0)
    val wiringPlusOne = Wiring.fromString("BCDEFGHIJKLMNOPQRSTUVWXYZA").require
    assert(wiringPlusOne.forward(p0.value) === p1)
    assert(wiringPlusOne.reverse(p1.value) === p0)
  }
}

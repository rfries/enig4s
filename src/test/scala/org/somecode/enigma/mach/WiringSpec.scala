package org.somecode.enigma
package mach

import scala.util.Left

import org.scalatest.EitherValues.*
import org.scalatest.matchers.should
import org.scalatest.wordspec.AnyWordSpec

class WiringSpec extends AnyWordSpec with should.Matchers:

  "Wiring" should {
    "allow creation with well-formed letter maps" in {
      Wiring.fromString("ABCDEFGHIJKLMNOPQRSTUVWXYZ").value shouldBe a [Wiring]
      Wiring.fromString("abCDfeGHIJKLMNpoQRSTUVWXYZ").value shouldBe a [Wiring]
      Wiring.fromString("ZABCDEFGHIJKLMNOPQRSTUVWXY").value shouldBe a [Wiring]
    }

    "fail creation with badly-formed letter maps" in {
      assert(Wiring.fromString("ABBDEFGHIJKLMNOPQRSTUVWXYZ").isLeft)
      assert(Wiring.fromString("ZABCDEFGHIJKLMNOPQRSTUVWXY12345").isLeft)
      assert(Wiring.fromString("ABCDEFGHIJKL-MNOPQRSTUVWXY").isLeft)
      assert(Wiring.fromString("").isLeft)
    }

    "translate positions according to the specified vector or letter map" in {
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

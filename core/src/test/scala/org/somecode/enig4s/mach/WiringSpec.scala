package org.somecode.enig4s
package mach

import scala.util.Left

import org.scalatest.AppendedClues
import org.scalatest.EitherValues.*
import org.scalatest.matchers.should
import org.scalatest.wordspec.AnyWordSpec

final class WiringSpec extends Enig4sSpec:

  "Wiring" should {
    "allow creation with well-formed letter maps" in {
      WiringSpecFixtures.goodWiringStrings.foreach {
        wstr =>
         Wiring(SymbolMap.AZ.stringToCodes(wstr).value).value shouldBe a [Wiring]
      }
    }

    "fail creation with badly-formed letter maps" in {
      WiringSpecFixtures.badWiringStrings.foreach {
        wstr => Wiring(SymbolMap.AZ.stringToCodes(wstr).value).isLeft shouldBe true
      }
    }

    "translate positions according to the specified vector or letter map" in {
      val p0 = Glyph.zero
      val p1 = Glyph.one

      val wiringId = Wiring(
        SymbolMap.AZ.stringToInts("ABCDEFGHIJKLMNOPQRSTUVWXYZ").value
      ).value

      assert(wiringId.wire(p0) === p0)
      assert(wiringId.inverse.wire(p0) === p0)

      val wiringPlusOne = Wiring(
        SymbolMap.AZ.stringToInts("BCDEFGHIJKLMNOPQRSTUVWXYZA").value
      ).value

      assert(wiringPlusOne.wire(p0) === p1)
      assert(wiringPlusOne.inverse.wire(p1) === p0)
    }
  }

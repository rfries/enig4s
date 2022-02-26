package org.somecode.enig4s
package mach

import scala.util.Left

import org.scalatest.AppendedClues
import org.scalatest.EitherValues.*
import org.scalatest.matchers.should
import org.scalatest.wordspec.AnyWordSpec

object WiringSpecFixtures:
  val goodWiring: Vector[Wiring] = Vector(
    Wiring(SymbolMap.AZ.stringToKeyCodes("ZABCDEFGHIJKLMNOPQRSTUVWXY").value).value,
    Wiring(SymbolMap.AZ.stringToKeyCodes("BCDEFGHIJKLMNOPQRSTUVWXYZA").value).value,
    Wiring(SymbolMap.AZ.stringToKeyCodes("ABCDE").value).value
  )

  val goodWiringStrings: Vector[String] = Vector(
    "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
    "ABCDFEGHIJKLMNPOQRSTUVWXYZ",
    "ZABCDEFGHIJKLMNOPQRSTUVWXY",
    "A"
  )

  val badWiringStrings: Vector[String] = Vector(
    "ADEFGHIJKLMNOPQRSTUVWXYZ",
    "ABCDZ",
    "ABBDEFGHIJKLMNOPQRSTUVWXYZ"
  )

class WiringSpec extends AnyWordSpec with should.Matchers with AppendedClues:

  "Wiring" should {
    "allow creation with well-formed letter maps" in {
      WiringSpecFixtures.goodWiringStrings.foreach {
        wstr =>
         Wiring(SymbolMap.AZ.stringToKeyCodes(wstr).value).value shouldBe a [Wiring]
      }
    }

    "fail creation with badly-formed letter maps" in {
      WiringSpecFixtures.badWiringStrings.foreach {
        wstr => Wiring(SymbolMap.AZ.stringToKeyCodes(wstr).value).isLeft shouldBe true
      }
    }

    "translate positions according to the specified vector or letter map" in {
      val p0 = KeyCode.zero
      val p1 = KeyCode(1).value

      val wiringId = Wiring(SymbolMap.AZ.stringToKeyCodes("ABCDEFGHIJKLMNOPQRSTUVWXYZ").value).value
      assert(wiringId.forward(p0.toInt) === p0)
      assert(wiringId.reverse(p0.toInt) === p0)
      val wiringPlusOne = Wiring(SymbolMap.AZ.stringToKeyCodes("BCDEFGHIJKLMNOPQRSTUVWXYZA").value).value
      assert(wiringPlusOne.forward(p0.toInt) === p1)
      assert(wiringPlusOne.reverse(p1.toInt) === p0)
    }
  }

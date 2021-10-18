package org.somecode.enigma
package mach

import scala.util.Left

import org.scalatest.AppendedClues
import org.scalatest.EitherValues.*
import org.scalatest.matchers.should
import org.scalatest.wordspec.AnyWordSpec

object WiringSpecFixtures:
  val goodWiring: Vector[Wiring] = Vector(
    Wiring.fromString("ZABCDEFGHIJKLMNOPQRSTUVWXY").value,
    Wiring.fromString("BCDEFGHIJKLMNOPQRSTUVWXYZA").value,
    Wiring.fromString("ABCDE").value
  )

  val goodWiringStrings: Vector[String] = Vector(
    "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
    "abCDfeGHIJKLMNpoQRSTUVWXYZ",
    "ZABCDEFGHIJKLMNOPQRSTUVWXY",
    "Q"
  )

  val badWiringStrings: Vector[String] = Vector(
    "ADEFGHIJKLMNOPQRSTUVWXYZ",
    "ABCDZ",
    "ABBDEFGHIJKLMNOPQRSTUVWXYZ",
    "ZABCDEFGHIJKLMNOPQRSTUVWXY12345",
    "ABCDEFGHIJKL-MNOPQRSTUVWXY",
    ""
  )

class WiringSpec extends AnyWordSpec with should.Matchers with AppendedClues:

  "Wiring" should {
    "allow creation with well-formed letter maps" in {
      WiringSpecFixtures.goodWiringStrings.foreach {
        wstr =>
         Wiring.fromString(wstr).value shouldBe a [Wiring]
      }
    }

    "fail creation with badly-formed letter maps" in {
      WiringSpecFixtures.badWiringStrings.foreach {
        wstr => assert(Wiring.fromString(wstr).isLeft, wstr)
      }
    }

    "translate positions according to the specified vector or letter map" in {
      val p0 = KeyCode.zero
      val p1 = KeyCode(1).value

      val wiringId = Wiring.fromString("ABCDEFGHIJKLMNOPQRSTUVWXYZ").value
      assert(wiringId.forward(p0.toInt) === p0)
      assert(wiringId.reverse(p0.toInt) === p0)
      val wiringPlusOne = Wiring.fromString("BCDEFGHIJKLMNOPQRSTUVWXYZA").value
      assert(wiringPlusOne.forward(p0.toInt) === p1)
      assert(wiringPlusOne.reverse(p1.toInt) === p0)
    }
  }

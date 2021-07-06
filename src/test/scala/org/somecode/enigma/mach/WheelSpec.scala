package org.somecode.enigma
package mach

import scala.util.Left

import org.scalatest.EitherValues.*
import org.scalatest.matchers.should
import org.scalatest.wordspec.AnyWordSpec

final class WheelSpec extends AnyWordSpec with should.Matchers:
  "Wheel" should {
    "allow creation with valid parameters" in {
      val wiring = Wiring.fromString("ZABCDEFGHIJKLMNOPQRSTUVWXY").value
      Wheel(wiring, KeyCode.unsafe(1), Set(KeyCode.unsafe(0), KeyCode.unsafe(13))).value shouldBe a [Wheel]
    }
  }

package org.somecode.enig4s
package mach

import scala.util.Left

import org.scalatest.EitherValues.*
import org.scalatest.matchers.should
import org.scalatest.wordspec.AnyWordSpec


final class WheelSpec extends AnyWordSpec with should.Matchers:

  "Wheel" should {
    "allow creation with valid parameters" in {
      val wiring = Wiring("ABCDEFGHIJKLMNOPQRSTUVWXYZ", SymbolMap.AZ).value
      Wheel(wiring, Vector(Glyph.zero), 0).value shouldBe a [Wheel]
    }
  }

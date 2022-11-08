package org.somecode.enig4s
package mach

import org.scalatest.EitherValues.*
import org.scalatest.OptionValues.*
import org.scalatest.matchers.*
import org.scalatest.wordspec.AnyWordSpec


final class GlyphSpec extends AnyWordSpec with should.Matchers {
  "Glyph" should {
    "use a BusSize for modulo math" in {
      given Modulus = Modulus(26).value

      val g11: Glyph = Glyph(11).value
      val g22: Glyph = Glyph(22).value
      val g0: Glyph = Glyph(0).value

      info(s"g1: $g11")
      info(s"g2: $g22")
      (g11 %+ g22).intVal shouldBe 7
      (g0 %- g11).intVal shouldBe 15
    }
  }
}

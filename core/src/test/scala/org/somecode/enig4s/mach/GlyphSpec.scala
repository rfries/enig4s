package org.somecode.enig4s
package mach

import org.scalatest.EitherValues.*
import org.scalatest.wordspec.AnyWordSpec

final class GlyphSpec extends Enig4sSpec {
  "Glyph" should {
    "use a BusSize for modulo math" in {
      given Modulus = Modulus(26).value

      val g11: Glyph = Glyph(11).value
      val g22: Glyph = Glyph(22).value
      val g0: Glyph = Glyph(0).value

      info(s"g1: $g11")
      info(s"g2: $g22")
      (g11 %+ g22).toInt shouldBe 7
      (g0 %- g11).toInt shouldBe 15
    }
  }
}

package org.somecode.enig4s.mach

import org.scalatest.matchers.should
import org.scalatest.EitherValues.*
import org.scalatest.wordspec.AnyWordSpec

class SymbolMapSpec extends AnyWordSpec with should.Matchers:

  "SymbolMap" should {
    "translate a valid String into a vector of Glyphs" in {
      val expected = Vector(0, 25, 7).map(Glyph.unsafe)
      SymbolMap.AZ.stringToGlyphs("AZH").value shouldBe expected
    }

    "translate a valid vector of Glyphs into a String" in {
      val glyphs = Vector(0, 25, 7).map(Glyph.unsafe)
      SymbolMap.AZ.glyphsToString(glyphs).value shouldBe "AZH"
    }

    "return an error when translating a String with invalid characters" in {
      SymbolMap.AZ.stringToGlyphs("A3H").isLeft shouldBe true
    }

    "return an error when translating a Vector with invalid Glyphs" in {
      val badGlyphs = Vector(1,2,99).map(Glyph.unsafe)
      SymbolMap.AZ.glyphsToString(badGlyphs).isLeft shouldBe true
    }
  }

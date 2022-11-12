package org.somecode.enig4s.mach

import org.scalatest.matchers.should
import org.scalatest.EitherValues.*
import org.scalatest.wordspec.AnyWordSpec

class SymbolMapSpec extends AnyWordSpec with should.Matchers:

  "SymbolMap" should {
    "translate a valid String into a vector of KeyCodes" in {
      val expected = Vector(0, 25, 7).map(KeyCode.unsafe)
      SymbolMap.AZ.stringToCodes("AZH").value shouldBe expected
    }

    "return an error when translating a String with invalid characters" in {
      SymbolMap.AZ.stringToCodes("A3H").isLeft shouldBe true
    }

    "translate a valid vector of KeyCodes into a String" in {
      val keyCodes = Vector(0, 25, 7).map(KeyCode.unsafe)
      SymbolMap.AZ.codesToString(keyCodes).value shouldBe "AZH"
    }

    "return an error when translating a Vector with invalid KeyCodes" in {
      val badCodes = Vector(1,2,99).map(KeyCode.unsafe)
      SymbolMap.AZ.codesToString(badCodes).isLeft shouldBe true
    }


    "translate a valid String into a vector of Glyphs" in {
      val expected = Vector(0, 25, 7).map(Glyph.unsafe)
      SymbolMap.AZ.stringToCodes("AZH").value shouldBe expected
    }

    "return an error when translating a String with invalid characters" in {
      SymbolMap.AZ.stringToCodes("A3H").isLeft shouldBe true
    }

    "translate a valid vector of Glyphs into a String" in {
      val Glyphs = Vector(0, 25, 7).map(Glyph.unsafe)
      SymbolMap.AZ.codesToString(Glyphs).value shouldBe "AZH"
    }

    "return an error when translating a Vector with invalid Glyphs" in {
      val badCodes = Vector(1,2,99).map(Glyph.unsafe)
      SymbolMap.AZ.codesToString(badCodes).isLeft shouldBe true
    }

  }

package org.somecode.enig4s.mach

import org.scalatest.matchers.should
import org.scalatest.EitherValues.*
import org.scalatest.wordspec.AnyWordSpec

class SymbolMapSpec extends AnyWordSpec with should.Matchers:

  "SymbolMap" should {
    "translate a valid String into a vector of KeyCodes" in {
      val expected = Vector(0, 25, 7).map(KeyCode.unsafe)
      SymbolMap.AZ.stringToKeyCodes("AZH").value shouldBe expected
    }
    "translate a valid vector of KeyCodes into a String" in {
      val keyCodes = Vector(0, 25, 7).map(KeyCode.unsafe)
      SymbolMap.AZ.keyCodesToString(keyCodes).value shouldBe "AZH"
    }
  }

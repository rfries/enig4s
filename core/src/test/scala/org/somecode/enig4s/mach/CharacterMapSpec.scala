package org.somecode.enig4s.mach

import org.scalatest.matchers.should
import org.scalatest.EitherValues.*
import org.scalatest.wordspec.AnyWordSpec

class CharacterMapSpec extends AnyWordSpec with should.Matchers:

  "CharacterMap" should {
    "translate a valid String into a vector of KeyCodes" in {
      val expected = Vector(0, 25, 7).map(KeyCode.unsafe)
      CharMap.AZ.stringToKeyCodes("AZH").value shouldBe expected
    }
    "translate a valid String into a ValidKeys" in {
      val expected = ValidKeys(26, Vector(0, 25, 7).map(KeyCode.unsafe)).value
      CharMap.AZ.stringToValidKeys("AZH").value shouldBe expected
    }
    "translate a valid vector of KeyCodes into a String" in {
      val keyCodes = Vector(0, 25, 7).map(KeyCode.unsafe)
      CharMap.AZ.keyCodesToString(keyCodes).value shouldBe "AZH"
    }
  }

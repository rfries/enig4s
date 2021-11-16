package org.somecode.enig4s.mach

import org.scalatest.matchers.should
import org.scalatest.EitherValues.*
import org.scalatest.wordspec.AnyWordSpec

class CharacterMapSpec extends AnyWordSpec with should.Matchers:

  "CharacterMap" should {
    "translate a valid String into a vector of KeyCodes" in {
      val charMap = CharacterMap("ABCDEFGHIJKLMNOPQRSTUVWXYZ").value
      val expected = Vector(0, 25, 7).map(KeyCode.unsafe)
      charMap.stringtoKeyCodes("AZH").value shouldBe expected
    }
    "translate a valid vector of KeyCodes into a String" in {
      val charMap: CharacterMap = CharacterMap("ABCDEFGHIJKLMNOPQRSTUVWXYZ").value
      val keyCodes = Vector(0, 25, 7).map(KeyCode.unsafe)
      charMap.keyCodesToString(keyCodes).value shouldBe "AZH"
    }
  }

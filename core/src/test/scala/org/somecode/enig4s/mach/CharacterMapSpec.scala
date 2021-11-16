package org.somecode.enig4s.mach

import org.scalatest.matchers.should
import org.scalatest.EitherValues.*
import org.scalatest.wordspec.AnyWordSpec

class CharacterMapSpec extends AnyWordSpec with should.Matchers:

  "CharacterMap" should {
    "translate a valid String into a vector of KeyCodes" in {
      val charMap = CharacterMap("ABCDEFGHIJKLMNOPQRSTUVWXYZ").value
      val expected = ValidKeys(26, Vector(0, 25, 7).map(KeyCode.unsafe)).value
      charMap.toKeyCodes("AZH").value shouldBe expected
    }
    "translate a valid vector of KeyCodes into a String" in {
      val charMap: CharacterMap = CharacterMap("ABCDEFGHIJKLMNOPQRSTUVWXYZ").value
    }
  }

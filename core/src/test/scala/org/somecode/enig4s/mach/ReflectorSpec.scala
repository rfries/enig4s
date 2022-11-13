package org.somecode.enig4s
package mach

import org.scalatest.matchers.should
import org.scalatest.EitherValues.*
import org.scalatest.wordspec.AnyWordSpec

class ReflectorSpec extends AnyWordSpec with should.Matchers:

  "reflector" should {
    "allow creation with valid parameters" in {
      val wiring = Wiring("ABC")
      wiring.isRight shouldBe true
      wiring.length shouldBe 3
      true
    }
  }

package org.somecode.enig4s.mach

import org.scalatest.EitherValues.*
import org.scalatest.matchers.should
import org.scalatest.wordspec.AnyWordSpec

class CabinetSpec extends AnyWordSpec with should.Matchers:
  "Cabinet" should {
    "initialize without error" in {
      Cabinet.init.isRight shouldBe true
    }
  }

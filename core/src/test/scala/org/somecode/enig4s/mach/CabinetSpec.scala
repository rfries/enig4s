package org.somecode.enig4s
package mach

import org.scalatest.EitherValues.*
import org.scalatest.OptionValues.*
import org.scalatest.matchers.should
import org.scalatest.wordspec.AnyWordSpec

class CabinetSpec extends AnyWordSpec with should.Matchers:

  val cabinet = Cabinet()

  "Cabinet" should {

    "initialize with default data without error" in {
      cabinet match
        case Left(err) => fail(s"Cabinet initialization failed: $err")
        case Right(cab) => ()
    }

    "fail initialization if initial data is invalid" in {
      val badWiringInit = Cabinet.defaultWiringInits :+ Cabinet.WiringInit("abc", "ZZZ", "abc")
      Cabinet(wiringInits = badWiringInit).isLeft shouldBe true
    }

    "look up a symbol map known to be present" in {
      cabinet.value.symbols.get("AZ").value shouldBe a [SymbolMap]
    }
    "look up a wiring known to be present" in {
      cabinet.value.wirings.get("I").value shouldBe a [Wiring]
    }
    "look up a wheel known to be present" in {
      cabinet.value.wheels.get("I").value shouldBe a [Wheel]
    }
    "look up a reflector known to be present" in {
      cabinet.value.reflectors.get("UKW-B").value shouldBe a [Reflector]
    }

    "return None when looking up a symbol map known not to be present" in {
      cabinet.value.symbols.get("ZZZ") shouldBe empty
    }
    "return None when looking up a wiring known not to be present" in {
      cabinet.value.wirings.get("ZZZ") shouldBe empty
    }
    "return None when looking up a wheel known not to be present" in {
      cabinet.value.wheels.get("ZZZ") shouldBe empty
    }
    "return None when looking up a reflector known not to be present" in {
      cabinet.value.reflectors.get("ZZZ") shouldBe empty
    }
  }

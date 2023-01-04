package org.somecode.enig4s
package jsapi

import org.scalatest.*
import org.scalatest.matchers.should.*
import org.scalatest.wordspec.AnyWordSpec
import io.circe.*
import io.circe.parser.*

final class MachineSpecJsSpec extends AnyWordSpec with Matchers:

  "MachineSpecJs" should {
    "decode" in {
      (3 + 3) shouldBe 6

      val js = """ { "n": 1 }"""

      val json = parse(js)

      json match
        case Left(pf) => fail("==> " + pf)
        case Right(_) => ()

    }
  }

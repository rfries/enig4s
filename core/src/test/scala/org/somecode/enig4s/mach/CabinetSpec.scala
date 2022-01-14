package org.somecode.enig4s
package mach

import org.scalatest.EitherValues.*
import org.scalatest.matchers.should
import org.scalatest.wordspec.AnyWordSpec

class CabinetSpec extends AnyWordSpec with should.Matchers:
  "Cabinet" should {
    "initialize without error" in {
      Cabinet.init match
        case Left(err) => fail(s"Cabinet initialization failed: $err")
        case Right(cab) => ()
    }
  }

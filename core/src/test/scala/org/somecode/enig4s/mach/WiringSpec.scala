package org.somecode.enig4s
package mach

import scala.util.Left

import org.scalatest.AppendedClues
import org.scalatest.Assertions.*
import org.scalatest.EitherValues.*
import org.scalatest.matchers.should
import org.scalatest.wordspec.AnyWordSpec
import scala.collection.immutable.ArraySeq

/* Note: also see WiringProps for property tests */

final class WiringSpec extends Enig4sSpec:

  import WiringSpec.*

  "Wiring (Seq Constructor)" should {
    "allow creation with well-formed sequences" in {
      checkWiring(ArraySeq(0))
      checkWiring(ArraySeq(0, 1))
      checkWiring(ArraySeq(1, 0))
      checkWiring(ArraySeq(4, 3, 2, 1, 0))
    }

    "fail creation with an empty sequence" in {
      Wiring(Seq.empty).left.value should include ("at least one")
    }

    "fail creation with duplicate values in the sequence" in {
      Wiring(Seq(0,1,2,1)).left.value should include ("duplicate")
    }

    "fail creation with non-continuous range in the sequence" in {
      Wiring(Seq(0,1,3,4)).left.value should include ("only values from")
      Wiring(Seq(1)).left.value should include ("only values from")
    }

    "fail creation with invalid values in the sequence" in {
      Wiring(Seq(0,1,-1)).left.value should include ("only values from")
    }
  }

  "Wiring (String Constructor)" should {

    "allow creation with well-formed strings" in {
      checkWiring("BAC") shouldBe a [Wiring]
      checkWiring("ABCDFEGHIJKLMNPOQRSTUVWXYZ") shouldBe a [Wiring]
      checkWiring("ZABCDEFGHIJKLMNOPQRSTUVWXY") shouldBe a [Wiring]
    }

    "fail creation with an empty string" in {
      Wiring("", SymbolMap.AZ).left.value should include ("at least one")
    }

    "fail creation with duplicate values in the string" in {
      Wiring("ABCA", SymbolMap.AZ).left.value should include ("duplicate")
    }

    "fail creation with non-continuous range in the string" in {
      Wiring("ABDE", SymbolMap.AZ).left.value should include ("only values from")
    }

    "fail creation with invalid values in the string" in {
      Wiring("A?B", SymbolMap.AZ).left.value should include ("Invalid character")
    }
  }


object WiringSpec:

  def checkWiring(seq: IndexedSeq[Int]): Wiring =
    withClue(s"Creating wiring with sequence $seq") {
      Wiring(seq) match
        case Left(s) =>
          fail(s"Failed to create wiring with sequence $seq")
        case Right(wiring) =>
          assert(wiring.length === seq.length)
          val glyphs = seq.map(Glyph.unsafe)
          glyphs.zipWithIndex foreach {
            (expected, n) =>
              val in = Glyph.unsafe(n)
              assert(wiring.wire(in) === expected)
              assert(wiring.inverse.wire(wiring.wire(in)) === in)
          }
          wiring
    }

  def checkWiring(mapping: String, symbols: SymbolMap = SymbolMap.AZ): Wiring =
    checkWiring(symbols.stringToInts(mapping).value)
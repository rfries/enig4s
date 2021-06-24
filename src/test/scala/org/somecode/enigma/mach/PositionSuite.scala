package org.somecode.enigma
package mach

import munit.ScalaCheckSuite
import org.scalacheck.Prop._
import org.scalacheck.Gen
import org.scalatest.EitherValues._

val posValGen: Gen[Int] = Gen.chooseNum(0, KeyCode.Max-1)
given posGen: Gen[KeyCode] = posValGen.map(KeyCode.unsafe)

class PositionSuite extends ScalaCheckSuite:

  test("Position creation") {
    assert(KeyCode(0).isInstanceOf[Right[String, KeyCode]])
    assert(KeyCode(2).isInstanceOf[Right[String, KeyCode]])
    assert(KeyCode(KeyCode.Max-1).isInstanceOf[Right[String, KeyCode]])
    assert(KeyCode(KeyCode.Max).isInstanceOf[Left[String, KeyCode]])
    intercept[java.lang.IllegalArgumentException] {
      KeyCode.unsafe(KeyCode.Max)
    }
    assert(KeyCode.unsafe(KeyCode.Max-1).toInt == KeyCode.Max - 1)
  }

  test("Position.next should advance/wrap over size boundry") {
    assert(KeyCode(0).value.next.toInt == 1)
    assert(KeyCode(1).value.next.toInt == 2)
    assert(KeyCode(KeyCode.Max-1).value.next.toInt == 0)
  }

  property("Position Addition Property") {
    forAll (posGen, posGen) { (p1: KeyCode, p2: KeyCode) =>
      assert( (p1 + p2).toInt == (p1.toInt + p2.toInt) % KeyCode.Max)
    }
  }

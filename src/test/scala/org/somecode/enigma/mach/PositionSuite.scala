package org.somecode.enigma
package mach

import munit.ScalaCheckSuite
import org.scalacheck.Prop._
import org.scalacheck.Gen

val posValGen: Gen[Int] = Gen.chooseNum(0, Position.Max-1)
given posGen: Gen[Position] = posValGen.map(Position.unsafe)

class PositionSuite extends ScalaCheckSuite:

  test("Position creation") {
    assert(Position(0).isInstanceOf[Right[String, Position]])
    assert(Position(2).isInstanceOf[Right[String, Position]])
    assert(Position(Position.Max-1).isInstanceOf[Right[String, Position]])
    assert(Position(Position.Max).isInstanceOf[Left[String, Position]])
    intercept[java.lang.IllegalArgumentException] {
      Position.unsafe(Position.Max)
    }
    assert(Position.unsafe(Position.Max-1) == Position.Max - 1)
  }

  test("Position.next should advance/wrap over size boundry") {
    assert(Position(0).require.next == 1)
    assert(Position(1).require.next == 2)
    assert(Position(Position.Max-1).require.next == 0)
  }

  property("Position Addition Property") {
    forAll (posGen, posGen) { (p1: Position, p2: Position) =>
      assert((p1 + p2) == (p1 + p2) % Position.Max)
    }
  }

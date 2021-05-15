package org.somecode.enigma
package mach

import munit.ScalaCheckSuite
import org.scalacheck.Prop._
import org.scalacheck.Gen
import org.scalacheck.Arbitrary
//import org.scalacheck.Gen._

val posValGen: Gen[Int] = Gen.chooseNum(0, Position.Max-1)
given posGen: Gen[Position] = posValGen.map(Position.unsafe)

class PositionPropSuite extends ScalaCheckSuite:

  //property("Position Valur Generator")
  property("Adding Positions") {
    forAll (posGen, posGen) { (p1: Position, p2: Position) =>
      assert((p1 + p2).value == (p1.value + p2.value) % Position.Max)
    }
  }


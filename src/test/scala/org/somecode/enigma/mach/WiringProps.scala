package org.somecode.enigma
package mach

import org.scalatest.propspec.AnyPropSpec
import org.scalacheck.Gen
import scala.util.Random
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class WiringProps extends AnyPropSpec with ScalaCheckDrivenPropertyChecks:
  // note: adding a type annotation here causes compiler crash w/ scala 3.0.0
  // i.e. val genPosVector: Gen[Vector[Position]] =
  val genPosVector = Gen.const((0 to Position.Max-1).toVector).map(Random.shuffle)

  // reflectors cannot have any straight-through mappings
  val genPosVectorReflect: Gen[Vector[Int]] = genPosVector suchThat
    (v => !v.zipWithIndex.exists((p, idx) => p == idx))

  property("Wiring translation (properties)") {
    forAll(genPosVector) { (v: Vector[Int]) =>
      whenever(v.length === Position.Max) {
        assert(v.length == Position.Max)
        val wiring = Wiring.fromPositions(v.map(Position.unsafe)).require
        (0 to Position.Max-1).foreach { n =>
          val forward = wiring.forward(n).value
          assert(forward == v(n))
          assert(wiring.reverse(forward).value == n+1)
        }
      }
    }
  }


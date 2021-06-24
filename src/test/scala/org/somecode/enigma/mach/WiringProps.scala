package org.somecode.enigma
package mach

import scala.util.Random

import org.scalacheck.Gen
import org.scalacheck.Shrink.shrinkAny      // disable shrinking, which ignores Gen.const generator
import org.scalatest.EitherValues.*
import org.scalatest.propspec.AnyPropSpec
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class WiringProps extends AnyPropSpec with ScalaCheckDrivenPropertyChecks:
  // note: adding a type annotation here causes compiler crash w/ scala 3.0.0
  // i.e. val genPosVector: Gen[Vector[Position]] =
  val genMappings = Gen.const((0 to KeyCode.Max-1).toVector).map(Random.shuffle)

  // reflectors cannot have any straight-through mappings
  val genReflectorMappings: Gen[Vector[Int]] = genMappings suchThat
    (v => !v.zipWithIndex.exists((p, idx) => p == idx))

  property("Wiring translation (properties)") {
    forAll(genMappings) { (v: Vector[Int]) =>
      whenever(v.length === KeyCode.Max) {
        val wiring = Wiring.fromPositions(v.map(KeyCode.unsafe)).value
        (0 to KeyCode.Max-1).foreach { n =>
          val forward = wiring.forward(n)
          assert(forward === v(n))
          assert(wiring.reverse(forward.toInt) === n)
        }
      }
    }
  }


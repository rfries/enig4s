package org.somecode.enig4s
package mach

import scala.util.Random
import org.scalacheck.Gen
import org.scalacheck.Shrink.shrinkAny
import org.scalatest.EitherValues.*
import org.scalatest.propspec.AnyPropSpec
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class WiringProps extends AnyPropSpec with ScalaCheckDrivenPropertyChecks:

  import WiringProps._
  // note: adding a type annotation here causes compiler crash w/ scala 3.0.0
  // i.e. val genPosVector: Gen[Vector[Position]] =

  property("Wiring translation (properties)") {

    // reflectors cannot have any straight-through mappings

    forAll(genMappings) { (v: Vector[KeyCode]) =>
      whenever(v.length === Max) {
        val wiring = Wiring(v).value
        (0 to Max-1).foreach { n =>
          val forward = wiring.forward(n)
          assert(forward === v(n))
          assert(wiring.reverse(forward.toInt) === n)
        }
      }
    }
  }

object WiringProps:
  val Max = 26
  val genMappings: Gen[Vector[KeyCode]] = Gen.const(
    (0 to Max-1).map(KeyCode.unsafe).toVector
  ).map(Random.shuffle)
  val genReflectorMappings: Gen[Vector[KeyCode]] = genMappings suchThat
    (v => !v.zipWithIndex.exists((p, idx) => p == idx))

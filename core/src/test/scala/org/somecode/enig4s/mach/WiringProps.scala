package org.somecode.enig4s
package mach

import cats.implicits.*
import org.scalacheck.Gen
import org.scalacheck.Shrink.shrinkAny
import org.scalatest.EitherValues.*
import org.scalatest.propspec.AnyPropSpec
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.collection.immutable.ArraySeq
import scala.util.Random

class WiringProps extends AnyPropSpec with ScalaCheckDrivenPropertyChecks:

  import WiringProps._

  property("Wiring translation (properties)") {

    // reflectors cannot have any straight-through mappings

    forAll(genMappings) { (v: ArraySeq[Int]) =>
      whenever(v.length === BusSize) {
        val wiring = Wiring(v).value
        (0 to BusSize-1).foreach { n =>
          val forward = wiring.wiring(n)
          assert(forward === v(n))
          assert(wiring.inverse.wire(forward) === n)
        }
      }
    }
  }

object WiringProps:
  val genMax = Gen.choose(1, 300)
  val BusSize = 26

  val genMappings: Gen[ArraySeq[Int]] = Gen.const((0 to BusSize-1).to(ArraySeq))
    .map(Random.shuffle)

  val genReflectorMappings: Gen[ArraySeq[Int]] = genMappings suchThat
    (v => !v.zipWithIndex.exists((p, idx) => p.toInt === idx))

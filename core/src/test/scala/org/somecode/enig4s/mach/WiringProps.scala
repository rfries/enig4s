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

    List(1, 2, 26, 100, 1000).foreach { busSize =>
      forAll(genMappings(busSize)) { (v: ArraySeq[Int]) =>
        whenever(v.length === busSize) {
          WiringSpec.checkWiring(v.map(Glyph.unsafe))
        }
      }
    }
  }

object WiringProps:

  def genMappings(size: Int): Gen[ArraySeq[Int]] =
    Gen.const((0 until size).to(ArraySeq)).map(Random.shuffle)

  // val genReflectorMappings: Gen[ArraySeq[Int]] = genMappings suchThat
  //   (v => !v.zipWithIndex.exists((p, idx) => p.toInt === idx))

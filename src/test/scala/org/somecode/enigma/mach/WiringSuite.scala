package org.somecode.enigma
package mach

import cats.Eq
import munit.ScalaCheckSuite
import org.scalacheck.Gen
import org.scalacheck.Gen._
import org.scalacheck.Prop._

import scala.util.Random

class WiringSuite extends ScalaCheckSuite:
  test("Wiring creation") {
    assert(Wiring.fromString("ABCDEFGHIJKLMNOPQRSTUVWXYZ").isInstanceOf[Right[String, Wiring]])
    assert(Wiring.fromString("ZABCDEFGHIJKLMNOPQRSTUVWXY").isInstanceOf[Right[String, Wiring]])

    assert(Wiring.fromString("ABBDEFGHIJKLMNOPQRSTUVWXYZ").isInstanceOf[Left[String, Wiring]])
    assert(Wiring.fromString("ZABCDEFGHIJKLMNOPQRSTUVWXY12345").isInstanceOf[Left[String, Wiring]])
    assert(Wiring.fromString("ABCDEFGHIJKL-MNOPQRSTUVWXY").isInstanceOf[Left[String, Wiring]])
    assert(Wiring.fromString("").isInstanceOf[Left[String, Wiring]])
  }

  test("Wiring translation") {
    val p0 = Position.zero
    val p1 = Position(1).require

    val wiringId = Wiring.fromString("ABCDEFGHIJKLMNOPQRSTUVWXYZ").require
    assert(wiringId.forward(p0.value) == p0)
    assert(wiringId.reverse(p0.value) == p0)
    val wiringPlusOne = Wiring.fromString("BCDEFGHIJKLMNOPQRSTUVWXYZA").require
    assert(wiringPlusOne.forward(p0.value) == p1)
    assert(wiringPlusOne.reverse(p1.value) == p0)
  }

  // note: adding type annotation here causes compiler crash w/ scala 3.0.0
  // i.e. val genPosVector: Gen[Vector[Position]] =
  val genPosVector = Gen.const((0 to Position.Max-1).toVector).map(Random.shuffle)

  // reflectors cannot have any straight-through mappings
  val genPosVectorReflect: Gen[Vector[Int]] = genPosVector suchThat
    (v => !v.zipWithIndex.exists((p, idx) => p == idx))

  property("Wiring translation (properties)") {
    forAll(genPosVector) { (v: Vector[Int]) =>
      // assertEquals(v.length, Position.Max)
      val wiring = Wiring.fromPositions(v.map(Position.unsafe)).require
      (0 to Position.Max-1).foreach { n =>
        val forward = wiring.forward(n).value
        assert(forward == v(n))
        assert(wiring.reverse(forward).value == n)
      }
    }
  }

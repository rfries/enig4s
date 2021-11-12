package org.somecode.enig4s
package mach

import org.scalacheck.Gen
import org.scalacheck.Shrink.shrinkAny      // disable shrinking, which ignores Gen.const generator
import org.scalatest.EitherValues.*
import org.scalatest.propspec.AnyPropSpec
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class KeyCodeProps extends AnyPropSpec with ScalaCheckDrivenPropertyChecks:

  def maxGen(max: Int): Gen[KeyCode] = Gen.chooseNum(0, max-1).map(KeyCode.unsafe)

  // test("Position creation") {
  //   val Max = 26
  //   assert(KeyCode(0).isInstanceOf[Right[String, KeyCode]])
  //   assert(KeyCode(2).isInstanceOf[Right[String, KeyCode]])
  //   assert(KeyCode(Max-1).isInstanceOf[Right[String, KeyCode]])
  //   assert(KeyCode(Max).isInstanceOf[Left[String, KeyCode]])
  //   intercept[java.lang.IllegalArgumentException] {
  //     KeyCode.unsafe(Max)
  //   }
  //   assert(KeyCode.unsafe(Max-1).toInt == Max - 1)
  // }

  // test("Position.next should advance/wrap over size boundry") {
  //   assert(KeyCode.unsafe(0).next == 1)
  //   assert(KeyCode.unsafe(1).next == 2)
  //   assert(KeyCode(KeyCode.Max-1).value.next.toInt == 0)
  // }

  // property("Position Addition Property") {
  //   val max = 26
  //   val gen = maxGen(max)
  //   forAll (gen, gen) { (p1: KeyCode, p2: KeyCode) =>
  //     assert( (p1.plusMod(p2, max)).toInt == (p1.toInt + p2.toInt) % max)
  //   }
  // }

package org.somecode.enig4s.mach

import org.scalatest.matchers.should
import org.scalatest.EitherValues
import org.scalatest.wordspec.AnyWordSpec

class NewNumSpec extends AnyWordSpec with should.Matchers with EitherValues:
  import NewNumSpec.*

  "NewNum based types" should {
    "Allow creation with valid values" in {
      Foo(3) shouldBe a [Right[String, Foo]]
      val foo: Foo = Foo.unsafe(3)
      Foo.unsafe(3) shouldBe foo
      Foo(3).value shouldBe foo
    }

    "Prevent creation with invalid values" in {
      Foo(-1) shouldBe a [Left[String, Foo]]
      an [IllegalArgumentException] should be thrownBy Foo.unsafe(-1)
    }

    "Enforce upper limit on Max types" in {
      val fooMax: FooMax = FooMax.unsafe(3)
      FooMax(3).value shouldBe fooMax
      FooMax(44) shouldBe a [Left[_,_]]
      an [IllegalArgumentException] should be thrownBy FooMax.unsafe(44)
      an [IllegalArgumentException] should be thrownBy FooMax.unsafe(-5)
    }
  }

object NewNumSpec:
  opaque type Foo <: Int = Int
  object Foo extends NewPosNum[Foo, Int]

  opaque type FooMax <: Int = Int
  object FooMax extends NewPozMaxNum[FooMax, Int](42)

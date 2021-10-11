package org.somecode.enigma.mach

import org.scalatest.matchers.*
import org.scalatest.wordspec.AnyWordSpec
import org.somecode.enigma.mach.Machine.MachineState
import org.somecode.enigma.mach.Machine.WheelState

final class MachineSpec extends AnyWordSpec with should.Matchers:

  def simpleWheels(ringSetting1: KeyCode, ringSetting2: KeyCode, ringSetting3: KeyCode): Vector[ConfiguredWheel] =
    Vector(
        Wheels.III.configure(ringSetting3).require,
        Wheels.II.configure(ringSetting2).require,
        Wheels.I.configure(ringSetting1).require
      )

  def simpleInitState: MachineState = MachineState(
      Vector(
        WheelState(KeyCode.zero),
        WheelState(KeyCode.zero),
        WheelState(KeyCode.zero)
      ),
      WheelState(KeyCode.zero)
    )

  "Machine" should {
    "encrypt a valid string with basic settings" in {
      val wheels = simpleWheels(KeyCode.zero, KeyCode.zero, KeyCode.zero)
      val reflector = Reflector(Wirings.B).require
      val in = ValidKeys("AAAAA").require

      Machine(wheels, reflector, Plugboard.empty,  Wirings.ETW) match
        case Right(mach) =>
          val (newState, out) = mach.crypt(simpleInitState, in).require
          out.toString shouldBe "BDZGO"
          info(s"$in => $out")
        case Left(msg) => fail("Failed to initialize Machine: $msg")
    }

    "encrypt a valid string with basic settings and plugs" in {
      val wheels = simpleWheels(KeyCode.zero, KeyCode.zero, KeyCode.zero)
      val reflector = Reflector(Wirings.B).require
      val in = ValidKeys("AAAAA").require
      val plugboard = Plugboard(26, Set("AZ", "SO", "FB")).require

      Machine(wheels, reflector, plugboard, Wirings.ETW) match
        case Right(mach) =>
          val (newState, out) = mach.crypt(simpleInitState, in).require
          out.toString shouldBe "UTZJY"
          info(s"$in => $out")
        case Left(msg) => fail("Failed to initialize Machine: $msg")
    }

    "encrypt a valid string with basic settings plus ring settings" in {
      val wheels = simpleWheels(KeyCode.one, KeyCode.one, KeyCode.one)
      val reflector = Reflector(Wirings.B).require
      val in = ValidKeys("AAAAA").require

      Machine(wheels, reflector, Plugboard.empty, Wirings.ETW) match
        case Right(mach) =>
          val (newState, out) = mach.crypt(simpleInitState, in).require
          out.toString shouldBe "EWTYX"
          info(s"$in => $out")
        case Left(msg) => fail("Failed to initialize Machine: $msg")
    }
  }

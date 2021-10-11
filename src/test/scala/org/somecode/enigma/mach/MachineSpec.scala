package org.somecode.enigma.mach

import org.scalatest.matchers.*
import org.scalatest.wordspec.AnyWordSpec
import org.somecode.enigma.mach.Machine.MachineState
import org.somecode.enigma.mach.Machine.WheelState

final class MachineSpec extends AnyWordSpec with should.Matchers:

  def simpleWheels(ringSetting1: Int, ringSetting2: Int, ringSetting3: Int): Vector[ConfiguredWheel] =
    Vector(
        Wheels.III.configure(KeyCode.unsafe(ringSetting3)).require,
        Wheels.II.configure(KeyCode.unsafe(ringSetting2)).require,
        Wheels.I.configure(KeyCode.unsafe(ringSetting1)).require
      )

  def initState(state1: Int, state2: Int, state3: Int, stateReflector: Int = 0): MachineState = MachineState(
      Vector(
        WheelState(KeyCode.unsafe(state3)),
        WheelState(KeyCode.unsafe(state2)),
        WheelState(KeyCode.unsafe(state1))
      ),
      WheelState(KeyCode.unsafe(stateReflector))
    )

  def simpleInitState: MachineState = initState(0, 0, 0, 0)

  def verifyText(mach: Machine, state: MachineState, in: ValidKeys, expected: String): Unit =
    val (newState, out) = mach.crypt(state, in).require
    out.toString shouldBe expected
    info(s"$in => $out")

  "Machine" should {
    "encrypt a valid string with basic settings" in {
      val wheels = simpleWheels(0, 0, 0)
      val reflector = Reflector(Wirings.B).require
      val in = ValidKeys("AAAAA").require

      Machine(wheels, reflector, Plugboard.empty,  Wirings.ETW) match
        case Right(mach) => verifyText(mach, simpleInitState, in, "BDZGO")
        case Left(msg) => fail("Failed to initialize Machine: $msg")
    }

    "encrypt a valid string with basic settings plus ring settings" in {
      val wheels = simpleWheels(1, 1, 1)
      val reflector = Reflector(Wirings.B).require
      val in = ValidKeys("AAAAA").require

      Machine(wheels, reflector, Plugboard.empty, Wirings.ETW) match
        case Right(mach) => verifyText(mach, simpleInitState, in, "EWTYX")
        case Left(msg) => fail("Failed to initialize Machine: $msg")
    }

    "encrypt a valid string with basic settings and plugs" in {
      val wheels = simpleWheels(0, 0, 0)
      val reflector = Reflector(Wirings.B).require
      val in = ValidKeys("AAAAA").require
      val plugboard = Plugboard(26, Set("AZ", "SO", "FB")).require

      Machine(wheels, reflector, plugboard, Wirings.ETW) match
        case Right(mach) => verifyText(mach, simpleInitState, in, "UTZJY")
        case Left(msg) => fail("Failed to initialize Machine: $msg")
    }

    "encrypt a valid string with more complex settings" in {
      val wheels = simpleWheels(14, 2, 22) // OCW
      val reflector = Reflector(Wirings.B).require
      val in = ValidKeys("ZELDA").require
      val plugboard = Plugboard(26, Set("SD", "FG", "HJ", "QY", "EC", "RV", "TB", "ZN", "UM")).require

      Machine(wheels, reflector, plugboard, Wirings.ETW) match
        case Right(mach) => verifyText(mach, initState(3, 7, 23), in, "NAWHM") // state: DHX
        case Left(msg) => fail("Failed to initialize Machine: $msg")
    }


  }

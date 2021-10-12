package org.somecode.enigma.mach

import org.scalatest.matchers.*
import org.scalatest.wordspec.AnyWordSpec
import org.somecode.enigma.mach.Machine.MachineState
import org.somecode.enigma.mach.Machine.WheelState

final class MachineSpec extends AnyWordSpec with should.Matchers:

  "Machine" should {
    "encrypt a string with basic wheel settings" in {
      val wheels = configureWheels(Wheels.I -> 0, Wheels.II -> 0, Wheels.III -> 0)
      val reflector = Reflector(Wirings.B).require
      val machState = machineState(Vector('A', 'A', 'A'))
      val in = ValidKeys("AAAAA").require

      Machine(wheels, reflector, Plugboard.empty,  Wirings.ETW) match
        case Right(mach) => verifyText(mach, machState, in, "BDZGO")
        case Left(msg) => fail("Failed to initialize Machine: $msg")
    }

    "encrypt a string with basic wheel settings plus ring settings" in {
      val wheels = configureWheels(Wheels.I -> 1, Wheels.II -> 1, Wheels.III -> 1)
      val reflector = Reflector(Wirings.B).require
      val machState = machineState(Vector('A', 'A', 'A'))
      val in = ValidKeys("AAAAA").require

      Machine(wheels, reflector, Plugboard.empty, Wirings.ETW) match
        case Right(mach) => verifyText(mach, machState, in, "EWTYX")
        case Left(msg) => fail("Failed to initialize Machine: $msg")
    }

    "encrypt a valid string with basic wheel settings and plugs" in {
      val wheels = configureWheels(Wheels.I -> 0, Wheels.II -> 0, Wheels.III -> 0)
      val reflector = Reflector(Wirings.B).require
      val machState = machineState(Vector('A', 'A', 'A'))
      val in = ValidKeys("AAAAA").require
      val plugboard = Plugboard(26, Set("AZ", "SO", "FB")).require

      Machine(wheels, reflector, plugboard, Wirings.ETW) match
        case Right(mach) => verifyText(mach, machState, in, "UTZJY")
        case Left(msg) => fail("Failed to initialize Machine: $msg")
    }

    "encrypt a valid string with more complex settings" in {
      val wheels = configureWheels(Wheels.I -> 14, Wheels.II -> 2, Wheels.III -> 22) // OCW
      val reflector = Reflector(Wirings.B).require
      val machState = machineState(Vector('D', 'H', 'X'))
      val in = ValidKeys("ZELDA").require
      val plugboard = Plugboard(26, Set("SD", "FG", "HJ", "QY", "EC", "RV", "TB", "ZN", "UM")).require

      Machine(wheels, reflector, plugboard, Wirings.ETW) match
        case Right(mach) => verifyText(mach, machState, in, "NAWHM")
        case Left(msg) => fail("Failed to initialize Machine: $msg")
    }

    "encrypt a valid string through a double-step sequence" in {
      val wheels = configureWheels(Wheels.III -> 0, Wheels.II -> 0, Wheels.I -> 0)
      val reflector = Reflector(Wirings.B).require
      val machState = machineState(Vector('K', 'D', 'O'))
      val in = ValidKeys("AAAAA").require

      Machine(wheels, reflector, Plugboard.empty, Wirings.ETW) match
        case Right(mach) =>
          val newState = verifyText(mach, machState, in, "ULMHJ")
          newState.wheelState shouldBe machineState(Vector('L', 'F', 'T')).wheelState
        case Left(msg) => fail("Failed to initialize Machine: $msg")
    }
  }

  def configureWheels(wheelConfigs: (Wheel, Int)*): Seq[ConfiguredWheel] =
    wheelConfigs.reverse.map((wheel, setting) => wheel.configure(KeyCode.unsafe(setting)).require)

  def machineState(wheelState: Vector[Char], reflectorState: Char = '\u0000'): MachineState =
    MachineState(
      wheelState.reverse.map(n => WheelState(KeyCode.unsafe(n - 'A'))),
      WheelState(KeyCode.unsafe(reflectorState))
    )

  def verifyText(mach: Machine, state: MachineState, in: ValidKeys, expected: String): MachineState =
    val (newState, out) = mach.crypt(state, in).require
    out.toString shouldBe expected
    info(s"$in => $out")
    newState

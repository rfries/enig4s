package org.somecode.enig4s
package mach

import org.scalatest.matchers.*
import org.scalatest.wordspec.AnyWordSpec
import org.somecode.enig4s.mach.Machine.{MachineState,WheelState}

final class MachineSpec extends AnyWordSpec with should.Matchers:

  "Machine" should {
    "encrypt a string with basic wheel settings" in {
      val wheels = configureWheels(Wheels.I -> 'A', Wheels.II -> 'A', Wheels.III -> 'A')
      val reflector = Reflector(Wirings.B).require
      val machState = machineState(Vector('A', 'A', 'A'))
      val in = "AAAAA"

      Machine(CharMap.AZ, wheels, reflector, Plugboard.empty,  Wirings.ETW) match
        case Right(mach) => verifyText(mach, machState, in, "BDZGO")
        case Left(msg) => fail("Failed to initialize Machine: $msg")
    }

    "encrypt a string with basic wheel settings plus ring settings" in {
      val wheels = configureWheels(Wheels.I -> 'B', Wheels.II -> 'B', Wheels.III -> 'B')
      val reflector = Reflector(Wirings.B).require
      val machState = machineState(Vector('A', 'A', 'A'))
      val in = "AAAAA"

      Machine(CharMap.AZ, wheels, reflector, Plugboard.empty, Wirings.ETW) match
        case Right(mach) => verifyText(mach, machState, in, "EWTYX")
        case Left(msg) => fail("Failed to initialize Machine: $msg")
    }

    "encrypt a valid string with basic wheel settings and plugs" in {
      val wheels = configureWheels(Wheels.I -> 'A', Wheels.II -> 'A', Wheels.III -> 'A')
      val reflector = Reflector(Wirings.B).require
      val machState = machineState(Vector('A', 'A', 'A'))
      val in = "AAAAA"
      val plugboard = Plugboard(26, Set("AZ", "SO", "FB")).require

      Machine(CharMap.AZ, wheels, reflector, plugboard, Wirings.ETW) match
        case Right(mach) => verifyText(mach, machState, in, "UTZJY")
        case Left(msg) => fail("Failed to initialize Machine: $msg")
    }

    "encrypt a valid string with more complex settings" in {
      val wheels = configureWheels(Wheels.I -> 'O', Wheels.II -> 'C', Wheels.III -> 'W')
      val reflector = Reflector(Wirings.B).require
      val machState = machineState(Vector('D', 'H', 'X'))
      val in = "ZELDA"
      val plugboard = Plugboard(26, Set("SD", "FG", "HJ", "QY", "EC", "RV", "TB", "ZN", "UM")).require

      Machine(CharMap.AZ, wheels, reflector, plugboard, Wirings.ETW) match
        case Right(mach) => verifyText(mach, machState, in, "NAWHM")
        case Left(msg) => fail("Failed to initialize Machine: $msg")
    }

    "encrypt a valid string through a double-step sequence" in {
      val wheels = configureWheels(Wheels.III -> 'A', Wheels.II -> 'A', Wheels.I -> 'A')
      val reflector = Reflector(Wirings.B).require
      val machState = machineState(Vector('K', 'D', 'O'))
      val in = "AAAAA"

      Machine(CharMap.AZ, wheels, reflector, Plugboard.empty, Wirings.ETW) match
        case Right(mach) =>
          val newState = verifyText(mach, machState, in, "ULMHJ")
          newState.wheelState shouldBe machineState(Vector('L', 'F', 'T')).wheelState
        case Left(msg) => fail("Failed to initialize Machine: $msg")
    }
  }

  def configureWheels(wheelConfigs: (Wheel, Char)*): Seq[Wheel] =
    wheelConfigs.reverse.map((wheel, setting) => wheel.copy(ringSetting = KeyCode(setting)).require)

  def machineState(wheelState: Vector[Char], reflectorState: Char = '\u0000'): MachineState =
    MachineState(
      wheelState.reverse.map(n => WheelState(KeyCode.unsafe(n - 'A'))),
      WheelState(KeyCode.unsafe(reflectorState))
    )

  def verifyText(mach: Machine, state: MachineState, in: String, expected: String): MachineState =
    val (newState, out) = mach.crypt(state, in).require
    out.toString shouldBe expected
    info(s"$in => $out")
    newState

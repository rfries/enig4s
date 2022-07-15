package org.somecode.enig4s
package mach

import org.scalatest.matchers.*
import org.scalatest.wordspec.AnyWordSpec

import scala.collection.immutable.ArraySeq

final class MachineSpec extends AnyWordSpec with should.Matchers:

  "Machine" should {
    "encrypt a string with basic wheel settings" in {
      val wheels = configureWheels(Wheels.I, Wheels.II, Wheels.III)
      val reflector = Reflector(Wirings.B).require
      val machState = machineState(Vector('A' -> 'A', 'A' -> 'A', 'A' -> 'A'))
      val in = "AAAAA"

      Machine(SymbolMap.AZ, Wirings.ETW, wheels, reflector, None) match
        case Right(mach) => verifyText(mach, machState, in, "BDZGO")
        case Left(msg) => fail(s"Failed to initialize Machine: $msg")
    }

    "encrypt a string with basic wheel settings plus ring settings" in {
      val wheels = configureWheels(Wheels.I, Wheels.II, Wheels.III)
      val reflector = Reflector(Wirings.B).require
      val machState = machineState(Vector('A' -> 'B', 'A' -> 'B', 'A' -> 'B'))
      val in = "AAAAA"

      Machine(SymbolMap.AZ, Wirings.ETW, wheels, reflector, None) match
        case Right(mach) => verifyText(mach, machState, in, "EWTYX")
        case Left(msg) => fail(s"Failed to initialize Machine: $msg")
    }

    "encrypt a valid string with basic wheel settings and plugs" in {
      val wheels = configureWheels(Wheels.I, Wheels.II, Wheels.III)
      val reflector = Reflector(Wirings.B).require
      val machState = machineState(Vector('A' -> 'A', 'A' -> 'A', 'A' -> 'A'))
      val in = "AAAAA"
      val plugboard = EnigmaPlugBoard(26, Vector("AZ", "SO", "FB"), SymbolMap.AZ).require

      Machine(SymbolMap.AZ, Wirings.ETW, wheels, reflector, Some(plugboard)) match
        case Right(mach) => verifyText(mach, machState, in, "UTZJY")
        case Left(msg) => fail(s"Failed to initialize Machine: $msg")
    }

    "encrypt a valid string with more complex settings" in {
      val wheels = configureWheels(Wheels.I, Wheels.II, Wheels.III)
      val reflector = Reflector(Wirings.B).require
      val machState = machineState(Vector('D' -> 'O', 'H' -> 'C', 'X' -> 'W'))
      val in = "ZELDA"
      val plugboard = EnigmaPlugBoard(26, Vector("SD", "FG", "HJ", "QY", "EC", "RV", "TB", "ZN", "UM"), SymbolMap.AZ).require

      Machine(SymbolMap.AZ, Wirings.ETW, wheels, reflector, Some(plugboard)) match
        case Right(mach) => verifyText(mach, machState, in, "NAWHM")
        case Left(msg) => fail(s"Failed to initialize Machine: $msg")
    }

    "encrypt a valid string through a double-step sequence" in {
      val wheels = configureWheels(Wheels.III, Wheels.II, Wheels.I)
      val reflector = Reflector(Wirings.B).require
      val machState = machineState(Vector('K' -> 'A', 'D' -> 'A', 'O' -> 'A'))
      val in = "AAAAA"

      Machine(SymbolMap.AZ, Wirings.ETW, wheels, reflector, None) match
        case Right(mach) =>
          val newState = verifyText(mach, machState, in, "ULMHJ")
          newState.wheelState shouldBe machineState(Vector('L' -> 'A', 'F' -> 'A', 'T' -> 'A')).wheelState
        case Left(msg) => fail(s"Failed to initialize Machine: $msg")
    }
  }

  def configureWheels(wheelConfigs: Wheel*): IndexedSeq[Wheel] = wheelConfigs.toIndexedSeq.reverse

  def machineState(wheelState: Vector[(Char, Char)], reflectorState: Char = '\u0000'): MachineState =
    MachineState(
      wheelState.reverse
        .zipWithIndex
        .map { case ((pos, rs), idx) =>
          WheelState(KeyCode.unsafe(pos - 'A'), RingSetting.unsafe(rs - 'A'))
        },
      Position.unsafe(reflectorState)
    )

  def verifyText(mach: Machine, state: MachineState, in: String, expected: String): MachineState =
    val res = mach.crypt(state, in, false).require
    res.text shouldBe expected
    info(s"$in => ${res.text}")
    res.state

package org.somecode.enigma.mach

import org.scalatest.matchers.*
import org.scalatest.wordspec.AnyWordSpec
import org.somecode.enigma.mach.Machine.MachineState
import org.somecode.enigma.mach.Machine.WheelState

final class MachineSpec extends AnyWordSpec with should.Matchers:
  "Machine" should {
    "encrypt a valid string with basic settings" in {
      val wheels = Vector(
        //Wheels.III.configure(KeyCode.unsafe(2)).require,
        Wheels.III.configure(KeyCode.zero).require,
        Wheels.II.configure(KeyCode.zero).require,
        Wheels.I.configure(KeyCode.zero).require
      )

      val reflector = Reflector(Wirings.B).require
      val mstate = MachineState(
        Vector(
          WheelState(KeyCode.zero),
          WheelState(KeyCode.zero),
          WheelState(KeyCode.zero)
        ),
        WheelState(KeyCode.zero)
      )

      val in = ValidKeys("AAAAA").require

      Machine(wheels, reflector, Wirings.ETW) match
        case Right(mach) =>
          val (newState, out) = mach.crypt(mstate, in).require
          out.toString shouldBe "BDZGO"
          info(s"$in => $out")
        case Left(msg) => fail("Failed to initialize Machine: $msg")
    }

    "encrypt a valid string with basic settings plus ring settings" in {
      val wheels = Vector(
        //Wheels.III.configure(KeyCode.unsafe(2)).require,
        Wheels.III.configure(KeyCode.one).require,
        Wheels.II.configure(KeyCode.one).require,
        Wheels.I.configure(KeyCode.one).require
      )

      val reflector = Reflector(Wirings.B).require
      val mstate = MachineState(
        Vector(
          WheelState(KeyCode.zero),
          WheelState(KeyCode.zero),
          WheelState(KeyCode.zero)
        ),
        WheelState(KeyCode.zero)
      )

      val in = ValidKeys("AAAAA").require

      Machine(wheels, reflector, Wirings.ETW) match
        case Right(mach) =>
          val (newState, out) = mach.crypt(mstate, in).require
          out.toString shouldBe "EWTYX"
          info(s"$in => $out")
        case Left(msg) => fail("Failed to initialize Machine: $msg")
    }
  }

trait Run:
  def run: Unit =
    Wheel("ZABCDEFGHIJKLMNOPQRSTUVWXY", Set("A", "N"))
      .flatMap(_.configure(KeyCode.unsafe(1))) match
      case Left(s) => throw new RuntimeException(s)
      case Right(wheel) =>
        println(wheel.toString)
        println(wheel.ringSetting)
      //summon[Show[Position]].show(rotor.ringSetting)
end Run
package org.somecode.enig4s
package mach

import cats.implicits.*
import org.scalatest.EitherValues.*
import org.scalatest.OptionValues.*
import org.scalatest.matchers.*
import org.scalatest.wordspec.AnyWordSpec

import scala.collection.immutable.ArraySeq

final class MachineSpec extends AnyWordSpec with should.Matchers:

  val cab: Cabinet = Cabinet().require

  "Machine" should {
    "(old) encrypt a string with basic wheel settings" in {
      val wheels = configureWheels(Wheels.I, Wheels.II, Wheels.III)
      val reflector = Reflector(Wirings.B).require
      val machState = machineState(Vector('A' -> 'A', 'A' -> 'A', 'A' -> 'A'))
      val in = "AAAAA"

      Machine(SymbolMap.AZ, Wirings.ETW, wheels, reflector, None) match
        case Right(mach) => verifyText(mach, machState, in, "BDZGO")
        case Left(msg) => fail(s"Failed to initialize Machine: $msg")
    }

    "encrypt a string with basic wheel settings (original)" in {
      val mach = machine(Vector("I", "II", "III"), "UKW-B", None).require
      val state = machState("AAA", "AAA").require

      verify(mach, state, "AAAAA", "BDZGO")
    }

    "encrypt a string with basic wheel settings" in {
      val mach = machine(Vector("I", "II", "III"), "UKW-B", None).require
      val state = machState(position = "ABC", ringSettings = "DEF", "A").require

      verify(mach, state, "ABCDE", "DXXVP")
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

  def machine(
      wheels: Vector[String],
      reflector: String,
      plugBoard: Option[Vector[String]] = None,
      symbols: SymbolMap = SymbolMap.AZ): Either[String, Machine] =
    for
      wh <- wheels.traverse(name => cab.findWheel(name).toRight(s"Wheel $name not found"))
      ref <- cab.findReflector(reflector).toRight(s"Reflector $reflector not found")
      plugs <- plugBoard.traverse(pb => EnigmaPlugBoard(ref.size, pb, symbols))
      mach <- Machine(symbols, Wiring.AZ, wh.reverse, ref, plugs)
    yield mach


  def machState(
      position: String,
      ringSettings: String,
      reflector: String = "A",
      symbols: SymbolMap = SymbolMap.AZ): Either[String, MachineState] =

    assert(reflector.size == 1)
    for
      pos <- symbols.stringToCodes(position).map(_.reverse)
      ring <- symbols.stringToCodes(ringSettings).map(_.reverse)
      _ <- Either.cond(ring.size === pos.size, (), "Position and ring settings strings must be the same length")
      ws = pos.zip(ring)
              .map((pos, ring) => WheelState(pos, RingSetting.unsafe(ring)))
      ref <- symbols.pointToCode(reflector.codePointAt(0)).map(Position.unsafe)
    yield MachineState(ws, ref)


  def configureWheels(wheelConfigs: Wheel*): IndexedSeq[Wheel] = wheelConfigs.toIndexedSeq.reverse

  def machineState(wheelState: Vector[(Char, Char)], reflectorState: Char = '\u0000'): MachineState =
    MachineState(
      wheelState.reverse
        .zipWithIndex
        .map { case ((pos, rs), idx) =>
          WheelState(KeyCode.unsafe(pos - 'A'), RingSetting.unsafe(rs - 'A'))
        }
        .to(ArraySeq),
      Position.unsafe(reflectorState)
    )

  def verifyText(mach: Machine, state: MachineState, in: String, expected: String): MachineState =
    val res = mach.crypt(state, in, true).require
    info(s"$in => ${res.text}")
    info(res.trace.map(_.mkString("\n")).getOrElse(""))
    res.text shouldBe expected
    res.state

  def verify(mach: Machine, state: MachineState, in: String, expected: String): Unit =
    val res = mach.crypt(state, in, true).require
    info(s"$in => ${res.text}")
    info(s"${res.trace.map(_.mkString("\n")).getOrElse("")}")
    res.text shouldBe expected

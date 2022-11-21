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

      Machine(SymbolMap.AZ, Entry.AZ, wheels, reflector, None) match
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

      Machine(SymbolMap.AZ, Entry.AZ, wheels, reflector, None) match
        case Right(mach) => verifyText(mach, machState, in, "EWTYX")
        case Left(msg) => fail(s"Failed to initialize Machine: $msg")
    }

    "encrypt a valid string with basic wheel settings and plugs" in {
      val wheels = configureWheels(Wheels.I, Wheels.II, Wheels.III)
      val reflector = Reflector(Wirings.B).require
      val machState = machineState(Vector('A' -> 'A', 'A' -> 'A', 'A' -> 'A'))
      val in = "AAAAA"
      val plugboard = EnigmaPlugBoard(26, Vector("AZ", "SO", "FB"), SymbolMap.AZ).require

      Machine(SymbolMap.AZ, Entry.AZ, wheels, reflector, Some(plugboard)) match
        case Right(mach) => verifyText(mach, machState, in, "UTZJY")
        case Left(msg) => fail(s"Failed to initialize Machine: $msg")
    }

    "encrypt a valid string with more complex settings" in {
      val wheels = configureWheels(Wheels.I, Wheels.II, Wheels.III)
      val reflector = Reflector(Wirings.B).require
      val machState = machineState(Vector('D' -> 'O', 'H' -> 'C', 'X' -> 'W'))
      val in = "ZELDA"
      val plugboard = EnigmaPlugBoard(26, Vector("SD", "FG", "HJ", "QY", "EC", "RV", "TB", "ZN", "UM"), SymbolMap.AZ).require

      Machine(SymbolMap.AZ, Entry.AZ, wheels, reflector, Some(plugboard)) match
        case Right(mach) => verifyText(mach, machState, in, "NAWHM")
        case Left(msg) => fail(s"Failed to initialize Machine: $msg")
    }

    "encrypt a valid string through a double-step sequence" in {
      val wheels = configureWheels(Wheels.III, Wheels.II, Wheels.I)
      val reflector = Reflector(Wirings.B).require
      val machState = machineState(Vector('K' -> 'A', 'D' -> 'A', 'O' -> 'A'))
      val in = "AAAAA"

      Machine(SymbolMap.AZ, Entry.AZ, wheels, reflector, None) match
        case Right(mach) =>
          val newState = verifyText(mach, machState, in, "ULMHJ")
          newState.wheelState shouldBe machineState(Vector('L' -> 'A', 'F' -> 'A', 'T' -> 'A')).wheelState
        case Left(msg) => fail(s"Failed to initialize Machine: $msg")
    }

  }

  def machine(
      wheels: Vector[String],
      rings: String,
      reflector: String,
      plugBoard: Option[Vector[String]] = None,
      symbols: SymbolMap = SymbolMap.AZ): Either[String, Machine] =
    for
      _ <- Either.cond(rings.length() === wheels.length, (), "Wheels vector and ring settings string must be the same length.")
      wh <- wheels.reverse.traverse(name => cab.findWheel(name).toRight(s"Wheel $name not found"))
      ringGlyphs <- symbols.stringToGlyphs(rings.reverse)
      cfgWheels <- wh.zip(ringGlyphs).traverse((wheel, glyph) => wheel.copy(ring = glyph))
      ref <- cab.findReflector(reflector).toRight(s"Reflector $reflector not found")
      plugs <- plugBoard.traverse(pb => EnigmaPlugBoard(ref.length, pb, symbols))
      mach <- Machine(symbols, Entry.AZ, cfgWheels, ref, plugs)
    yield mach


  def machState(
      position: String,
      reflector: String = "A",
      symbols: SymbolMap = SymbolMap.AZ): Either[String, MachineState] =

    assert(reflector.size == 1)
    for
      pos <- symbols.stringToGlyphs(position).map(_.reverse)
      ref <- symbols.pointToGlyph(reflector.codePointAt(0))
    yield MachineState(pos, ref)


  def configureWheels(wheelConfigs: Wheel*): IndexedSeq[Wheel] = wheelConfigs.toIndexedSeq.reverse

  def machineState(wheelState: Vector[(Char, Char)]): MachineState =
    MachineState(
      wheelState.reverse
        .zipWithIndex
        .map { case ((pos, rs), idx) =>
          WheelState(Glyph.unsafe(pos - 'A'), RingSetting.unsafe(rs - 'A'))
        }
        .to(ArraySeq),
      Glyph.zero
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

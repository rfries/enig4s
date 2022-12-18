package org.somecode.enig4s
package mach

import cats.implicits.*
import org.scalatest.EitherValues.*
import org.scalatest.OptionValues.*
import org.scalatest.matchers.*
import org.scalatest.wordspec.AnyWordSpec

import scala.collection.immutable.ArraySeq

final class MachineSpec extends AnyWordSpec with should.Matchers:

  import MachineSpec.*

  "encrypt a string with basic wheel settings (original)" in {
    val mach = machine(wheels = Vector("I", "II", "III"), reflector = "UKW-B").require
    val state = machState(position = "AAA", rings = "AAA").require

    verify(mach, state, "AAAAA", "BDZGO")
  }

  "encrypt a string with basic wheel and ring settings" in {
    val mach = machine(wheels = Vector("I", "II", "III"), reflector = "UKW-B").require
    val state = machState(position = "ABC", rings = "DEF", "A").require
    println(s">>> $state")

    verify(mach, state, "ABCDE", "DXXVP")
  }

  "encrypt a string with basic wheel settings plus ring settings" in {
    val mach = machine(wheels = Vector("I", "II", "III"), reflector = "UKW-B").require
    val state = machState(position = "AAA", rings = "BBB", "A").require

    verify(mach, state, "AAAAA", "EWTYX")
  }

  "encrypt a valid string with basic wheel settings and plugs" in {
    val mach = machine(wheels = Vector("I", "II", "III"), reflector = "UKW-B", Some(Vector("AZ", "SO", "FB"))).require
    val state = machState(position = "AAA", rings = "AAA", "A").require
    verify(mach, state, "AAAAA", "UTZJY")
  }

  "encrypt a valid string with more complex settings" in {
    val mach = machine(wheels = Vector("I", "II", "III"), reflector = "UKW-B",
      plugboard = Some(Vector("SD", "FG", "HJ", "QY", "EC", "RV", "TB", "ZN", "UM"))).require
    val state = machState(position = "DHX", rings = "OCW", "A").require

    verify(mach, state, "ZELDA", "NAWHM")
  }

  "encrypt a valid string through a double-step sequence" in {
    val mach = machine(wheels = Vector("III", "II", "I"), reflector = "UKW-B").require
    val state = machState(position = "KDO", rings = "AAA", "A").require

    val newState = verify(mach, state, "AAAAA", "ULMHJ")
    SymbolMap.AZ.glyphsToString(newState.positions).require.reverse shouldBe "LFT"
  }

  def verifyText(mach: Machine, state: MachineState, in: String, expected: String): MachineState =
    val res = mach.crypt(state, in, true).require
    info(s"$in => ${res.text}")
    info(res.trace.map(_.mkString("\n")).getOrElse(""))
    res.text shouldBe expected
    res.state

  def verify(mach: Machine, state: MachineState, in: String, expected: String): MachineState =
    val res = mach.crypt(state, in, true).require
    info(s"$in => ${res.text}")
    info(s"${res.trace.map(_.mkString("\n")).getOrElse("")}")
    res.text shouldBe expected
    res.state

object MachineSpec:

  val cab: Cabinet = Cabinet().require
  val entryAz: Entry = Entry.passthrough(26).require

  def machine(
      wheels: Vector[String],
      reflector: String,
      plugboard: Option[Vector[String]] = None,
      symbols: SymbolMap = SymbolMap.AZ): Either[String, Machine] =
    for
      wh <- wheels.reverse.traverse(name => cab.findWheel(name).toRight(s"Wheel $name not found"))
      cfgWheels <- wh.zipWithIndex.traverse((wheel, wheelNum) => wheel.copy(wheelNum = wheelNum))
      ref <- cab.findReflector(reflector).toRight(s"Reflector $reflector not found")
      plugs <- plugboard.traverse(pb => EnigmaPlugBoard(ref.length, pb, symbols))
      mach <- Machine(entryAz, cfgWheels, ref, plugs, symbols)
    yield mach

  def machState(
        position: String,
        rings: String,
        reflector: String = "A",
        symbols: SymbolMap = SymbolMap.AZ): Either[String, MachineState] =

    for
      pos <- symbols.stringToGlyphs(position).map(_.reverse)
      ring <- symbols.stringToGlyphs(rings).map(_.reverse)
      ref <- symbols.pointToGlyph(reflector.codePointAt(0))
    yield MachineState(pos, ring, ref, symbols)

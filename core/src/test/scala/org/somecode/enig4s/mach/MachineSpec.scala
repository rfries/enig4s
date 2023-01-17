package org.somecode.enig4s
package mach

import cats.implicits.*

import scala.collection.immutable.ArraySeq

final class MachineSpec extends Enig4sSpec:

  import MachineSpec.*

  "encrypt a string with basic wheel settings" in {
    val mach = machine(wheels = Vector("I", "II", "III"), reflector = "UKW-B").require
    val state = machState(position = "AAA", rings = "AAA").require

    verify(mach, state, "AAAAA", "BDZGO")
  }

  "encrypt a string with wheel and ring settings" in {
    val mach = machine(wheels = Vector("I", "II", "III"), reflector = "UKW-B").require
    val state = machState(position = "ABC", rings = "DEF").require

    verify(mach, state, "ABCDE", "DXXVP")
  }

  "encrypt a valid string with plugboard" in {
    val mach = machine(wheels = Vector("I", "II", "III"), reflector = "UKW-B").require
    val state = machState(position = "AAA", rings = "AAA", plugboard = Some(Vector("AZ", "SO", "FB"))).require
    verify(mach, state, "AAAAA", "UTZJY")
  }

  "encrypt a valid string with more complex settings" in {
    val mach = machine(wheels = Vector("I", "II", "III"), reflector = "UKW-B").require
    val state = machState(position = "DHX", rings = "OCW",
      plugboard = Some(Vector("SD", "FG", "HJ", "QY", "EC", "RV", "TB", "ZN", "UM"))).require

    verify(mach, state, "ZELDA", "NAWHM")
  }

  "encrypt a valid string through a double-step sequence" in {
    val mach = machine(wheels = Vector("III", "II", "I"), reflector = "UKW-B").require
    val state = machState(position = "KDO", rings = "AAA").require

    val newState = verify(mach, state, "AAAAA", "ULMHJ")
    SymbolMap.AZ.glyphsToString(newState.positions).require.reverse shouldBe "LFT"
  }

  "decrypt the message key from the 1945 Dönitz message" in {
    val mach = machine(wheels = Vector("m4.BETA", "V", "VI", "VIII"), reflector = "m4.UKW-C").require
    val plugs = Some(Vector("AE", "BF", "CM", "DQ", "HU", "JN", "LX", "PR", "SZ", "VW"))
    val state = machState(position = "NAEM", rings = "EPEL", plugboard = plugs).require
    verify(mach, state, "QEOB", "CDSZ")
  }

  def verify(mach: Machine, state: MachineState, in: String, expected: String): MachineState =
    val res = mach.crypt(state, in).require
    info(s"$in => ${res.text}")
    info(res.trace.getOrElse(""))
    res.text shouldBe expected
    res.state

object MachineSpec:

  val cab: Cabinet = Cabinet().require
  val entryAz: Entry = Entry.passthrough(26).require

  def machine(
      wheels: Vector[String],
      reflector: String,
      symbols: SymbolMap = SymbolMap.AZ): Either[String, Machine] =
    for
      wh <- wheels.reverse.traverse(name => cab.findWheel(name).toRight(s"Wheel $name not found"))
      cfgWheels <- wh.zipWithIndex.traverse((wheel, wheelNum) => wheel.copy(wheelNum = wheelNum))
      ref <- cab.findReflector(reflector).toRight(s"Reflector $reflector not found")
      mach <- Machine(entryAz, cfgWheels, ref, symbols)
    yield mach

  def machState(
        position: String,
        rings: String,
        reflector: String = "A",
        plugboard: Option[Vector[String]] = None,
        symbols: SymbolMap = SymbolMap.AZ): Either[String, MachineState] =

    for
      pos <- symbols.stringToGlyphs(position).map(_.reverse)
      ring <- symbols.stringToGlyphs(rings).map(_.reverse)
      ref <- symbols.pointToGlyph(reflector.codePointAt(0))
      plugs <- plugboard.traverse(pb => EnigmaPlugBoard(symbols.size, pb, symbols))
    yield MachineState(pos, ring, ref, plugs, symbols).withTrace

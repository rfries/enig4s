package org.somecode.enig4s
package mach

import cats.implicits.*

type Symbols    = Map[String, SymbolMap]
type Wirings    = Map[String, Wiring]
type Wheels     = Map[String, Wheel]
type Reflectors = Map[String, Reflector]

case class Cabinet private (
  symbols: Symbols,
  wirings: Wirings,
  wheels: Wheels,
  reflectors: Reflectors,
  symbolInits: Vector[Cabinet.SymbolMapInit],
  wiringInits: Vector[Cabinet.WiringInit],
  wheelInits: Vector[Cabinet.WheelInit],
  reflectorInits: Vector[Cabinet.ReflectorInit]
)

object Cabinet:

  case class SymbolMapInit(name: String, mapping: String)
  case class WiringInit(name: String, symbols: String, mapping: String)
  case class WheelInit(name: String, wiring: String, symbols: String, notches: String)
  case class ReflectorInit(name: String, wiring: String, positions: String = "A")

  def apply(
      symbolMapInits: Vector[SymbolMapInit] = defaultSymbolMapInits,
      wiringInits: Vector[WiringInit] = defaultWiringInits,
      wheelInits: Vector[WheelInit] = defaultWheelInits,
      reflectorInits: Vector[ReflectorInit] = defaultReflectorInits,
  ): Either[String, Cabinet] = {

    def initSymbolMaps: Either[String, Symbols] =
      symbolMapInits
        .map(cmi => SymbolMap(cmi.mapping).map(cm => (cmi.name, cm)))
        .sequence
        .map(_.toMap)

    def initWirings(symMaps: Symbols): Either[String, Wirings] =
      val pairs: Vector[Either[String, (String, Wiring)]] =
        for
          wi <- wiringInits
        yield
          for
            symMap <- symMaps.get(wi.symbols).toRight(s"Symbol map '${wi.symbols}' not defined.")
            keyCodes <- symMap.stringToCodes(wi.mapping)
            wiring <- Wiring(keyCodes).map(w => (wi.name, w))
          yield wiring
      // turn inside-out and then to a map:
      //   Vector[Either[String, (String, Wiring)]] =>  Either[String, Vector[(String, Wiring]]
      //   then, if Right, Vector[(String, Wiring)] => Map[String, Wiring]
      pairs.sequence.map(_.toMap)

    def initWheels(symMaps: Symbols, wirings: Wirings): Either[String, Wheels] =
      val pairs: Vector[Either[String, (String, Wheel)]] =
        for
          winit <- wheelInits
        yield
          for
            symMap <- symMaps.get(winit.symbols).toRight(s"Symbol map '${winit.symbols}' not defined.")
            wiring <- wirings.get(winit.wiring).toRight(s"Wiring '${winit.wiring}' not defined.")
            wheel <- Wheel(wiring, winit.notches, symMap)
          yield (winit.name, wheel)
      pairs.sequence.map(_.toMap)

    def initReflectors(wirings: Wirings): Either[String, Reflectors] =
      reflectorInits
        .map( rinit =>
          wirings.get(rinit.wiring)
            .toRight(s"Wiring '${rinit.wiring}' not defined.")
            .flatMap(wiring => Reflector(wiring, None).map(ref => (rinit.name, ref)))
        )
        .sequence
        .map(_.toMap)

    for
      symbols <- initSymbolMaps
      wirings <- initWirings(symbols)
      wheels <- initWheels(symbols, wirings)
      reflectors <- initReflectors(wirings)
    yield
      Cabinet(symbols, wirings, wheels, reflectors,
        symbolMapInits, wiringInits, wheelInits, reflectorInits)
  }

  val defaultSymbolMapInits: Vector[SymbolMapInit] = Vector(
    SymbolMapInit("AZ", "ABCDEFGHIJKLMNOPQRSTUVWXYZ"),
    SymbolMapInit("10", "1234567890")
  )

  val defaultWheelInits: Vector[WheelInit] = Vector(
    //        name          wiring        symbolMap notches

    WheelInit("I",          "I",          "AZ",   "Q"),
    WheelInit("II",         "II",         "AZ",   "E"),
    WheelInit("III",        "III",        "AZ",   "V"),
    WheelInit("IV",         "IV",         "AZ",   "J"),
    WheelInit("V",          "V",          "AZ",   "Z"),
    WheelInit("m3.VI",      "m3.VI",      "AZ",   "ZM"),
    WheelInit("m3.VII",     "m3.VII",     "AZ",   "ZM"),
    WheelInit("m3.VIII",    "m3.VIII",    "AZ",   "ZM"),
    WheelInit("m4.BETA",    "m4.BETA",    "AZ",   ""),
    WheelInit("m4.GAMMA",   "m4.GAMMA",   "AZ",   ""),
    WheelInit("d.I",        "d.I",        "AZ",   "Y"),
    WheelInit("d.II",       "d.II",       "AZ",   "E"),
    WheelInit("d.III",      "d.III",      "AZ",   "N"),
    WheelInit("g.I",        "d.I",        "AZ",   "SUVWZABCEFGIKLOPQ"),
    WheelInit("g.II",       "d.II",       "AZ",   "STVYZACDFGHKMNQ"),
    WheelInit("g.III",      "d.III",      "AZ",   "UWXAEFHKMNR"),
    WheelInit("r.I",        "r.I",        "AZ",   "N"),
    WheelInit("r.II",       "r.II",       "AZ",   "E"),
    WheelInit("r.III",      "r.III",      "AZ",   "Y"),
    WheelInit("z.I",        "z.I",        "10",   "9"),
    WheelInit("z.II",       "z.II",       "10",   "9"),
    WheelInit("z.III",      "z.III",      "10",   "9"),
  )

  val defaultReflectorInits: Vector[ReflectorInit] = Vector(
  //              name     wiring
    ReflectorInit("UKW-A",      "UKW-A"),
    ReflectorInit("UKW-B",      "UKW-B"),
    ReflectorInit("UKW-C",      "UKW-C"),
    ReflectorInit("m4.UKW-B",   "m4.UKW-B"),
    ReflectorInit("m4.UKW-C",   "m4.UKW-C"),
    ReflectorInit("d.UKW",      "d.UKW"),
    ReflectorInit("r.UKW",      "r.UKW"),
    ReflectorInit("g312.UKW",   "g312.UKW"),
    ReflectorInit("z.UKW",      "z.UKW"),
  )

  val defaultWiringInits: Vector[WiringInit] = Vector(

    // Enigma I
    WiringInit("ETW",       "AZ",   "ABCDEFGHIJKLMNOPQRSTUVWXYZ"),

    WiringInit("I",         "AZ",   "EKMFLGDQVZNTOWYHXUSPAIBRCJ"),
    WiringInit("II",        "AZ",   "AJDKSIRUXBLHWTMCQGZNPYFVOE"),
    WiringInit("III",       "AZ",   "BDFHJLCPRTXVZNYEIWGAKMUSQO"),
    WiringInit("IV",        "AZ",   "ESOVPZJAYQUIRHXLNFTGKDCMWB"),
    WiringInit("V",         "AZ",   "VZBRGITYUPSDNHLXAWMJQOFECK"),

    WiringInit("UKW-A",     "AZ",   "EJMZALYXVBWFCRQUONTSPIKHGD"),
    WiringInit("UKW-B",     "AZ",   "YRUHQSLDPXNGOKMIEBFZCWVJAT"),
    WiringInit("UKW-C",     "AZ",   "FVPJIAOYEDRZXWGCTKUQSBNMHL"),

    // M3 & M4
    WiringInit("m3.VI",     "AZ",   "JPGVOUMFYQBENHZRDKASXLICTW"),
    WiringInit("m3.VII",    "AZ",   "NZJHGRCXMYSWBOUFAIVLPEKQDT"),
    WiringInit("m3.VIII",   "AZ",   "FKQHTLXOCBJSPDZRAMEWNIUYGV"),

    // M4
    WiringInit("m4.BETA",   "AZ",   "LEYJVCNIXWPBQMDRTAKZGFUHOS"),
    WiringInit("m4.GAMMA",  "AZ",   "FSOKANUERHMBTIYCWLQPZXVGJD"),
    WiringInit("m4.UKW-B",  "AZ",   "ENKQAUYWJICOPBLMDXZVFTHRGS"),
    WiringInit("m4.UKW-C",  "AZ",   "RDOBJNTKVEHMLFCWZAXGYIPSUQ"),

    // Enigma D (A26), K & G (Zählwerk Enigma)
    WiringInit("d.ETW",     "AZ",   "QWERTZUIOASDFGHJKPYXCVBNML"),
    WiringInit("d.I",       "AZ",   "LPGSZMHAEOQKVXRFYBUTNICJDW"),
    WiringInit("d.II",      "AZ",   "SLVGBTFXJQOHEWIRZYAMKPCNDU"),
    WiringInit("d.III",     "AZ",   "CJGDPSHKTURAWZXFMYNQOBVLIE"),
    WiringInit("d.UKW",     "AZ",   "IMETCGFRAYSQBZXWLHKDVUPOJN"),

    // Enigma K - Swiss Variant ("Swiss K")
    WiringInit("sk.ETW",    "AZ",   "QWERTZUIOASDFGHJKPYXCVBNML"),
    WiringInit("sk.I",      "AZ",   "PEZUOHXSCVFMTBGLRINQJWAYDK"),
    WiringInit("sk.II",     "AZ",   "ZOUESYDKFWPCIQXHMVBLGNJRAT"),
    WiringInit("sk.III",    "AZ",   "EHRVXGAOBQUSIMZFLYNWKTPDJC"),

    // Enigma G-312 (One of several Enigma G wirings used by the Abwehr)
    WiringInit("g312.I",    "AZ",   "DMTWSILRUYQNKFEJCAZBPGXOHV"),
    WiringInit("g312.II",   "AZ",   "HQZGPJTMOBLNCIFDYAWVEUSRKX"),
    WiringInit("g312.III",  "AZ",   "UQNTLSZFMREHDPXKIBVYGJCWOA"),
    WiringInit("g312.UKW",  "AZ",   "RULQMZJSYGOCETKWDAHNBXPVIF"),

    // Enigma G-260 (One of several Enigma G wirings used by the Abwehr)
    WiringInit("g260.I",    "AZ",   "RCSPBLKQAUMHWYTIFZVGOJNEXD"),
    WiringInit("g260.II",   "AZ",   "WCMIBVPJXAROSGNDLZKEYHUFQT"),
    WiringInit("g260.III",  "AZ",   "FVDHZELSQMAXOKYIWPGCBUJTNR"),

    // Railway Enigma (Rocket)
    WiringInit("r.ETW",     "AZ",   "QWERTZUIOASDFGHJKPYXCVBNML"),
    WiringInit("r.I",       "AZ",   "JGDQOXUSCAMIFRVTPNEWKBLZYH"),
    WiringInit("r.II",      "AZ",   "NTZPSFBOKMWRCJDIVLAEYUXHGQ"),
    WiringInit("r.III",     "AZ",   "JVIUBHTCDYAKEQZPOSGXNRMWFL"),
    WiringInit("r.UKW",     "AZ",   "QYHOGNECVPUZTFDJAXWMKISRBL"),

    // Enigma Z
    WiringInit("z.ETW",     "10",   "1234567890"),
    WiringInit("z.I",       "10",   "6418270359"),
    WiringInit("z.II",      "10",   "5841097632"),
    WiringInit("z.III",     "10",   "3581620794"),
    WiringInit("z.UKW",     "10",   "5079183642"),
  )

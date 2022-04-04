package org.somecode.enig4s
package mach

import cats.implicits.*

type Symbols    = Map[String, SymbolMap]
type Wirings    = Map[String, Wiring]
type Wheels     = Map[String, Wheel]
type Reflectors = Map[String, Reflector]

case class Cabinet(
  symbols: Symbols,
  wirings: Wirings,
  wheels: Wheels,
  reflectors: Reflectors,
)

object Cabinet:

  case class SymbolsInit(name: String, mapping: String)
  case class WiringInit(name: String, symbols: String, mapping: String)
  case class WheelInit(name: String, wiringName: String, notches: String, ringSetting: KeyCode)
  case class ReflectorInit(name: String, wiringName: String, positions: String, advance: Boolean)

  def init: Either[String, Cabinet] =
    for
      symbols <- initCharMaps
      wirings <- initWirings(symbols)
      wheels <- initWheels(wirings)
      reflectors <- initReflectors(wirings)
    yield
      Cabinet(symbols, wirings, wheels, reflectors)

  def initCharMaps: Either[String, Symbols] =
    symbolsInit
      .map(cmi => SymbolMap(cmi.mapping).map(cm => (cmi.name, cm)))
      .sequence
      .map(_.toMap)

  def initWirings(charMaps: Symbols): Either[String, Wirings] =
    val pairs: Vector[Either[String, (String, Wiring)]] =
      for
        wi <- wiringInit
      yield for
        charMap <- charMaps.get(wi.symbols).toRight(s"Character map '${wi.symbols}' not defined.")
        keyCodes <- charMap.stringToCodes(wi.mapping)
        wiring <- Wiring(keyCodes).map(w => (wi.name, w))
      yield wiring
    // turn inside-out and then to a map:
    //   Vector[Either[String, (String, Wiring)]] =>  Either[String, Vector[(String, Wiring]]
    //   then, if Right, Vector[(String, Wiring)] => Map[String, Wiring]
    pairs.sequence.map(_.toMap)

  def initWheels(wirings: Wirings): Either[String, Wheels] =
    wheelInit
      .map( winit =>
        wirings
          .get(winit.wiringName)
          .toRight(s"Wiring '${winit.wiringName}' not defined.")
          .flatMap(wiring => Wheel(wiring, winit.notches, SymbolMap.AZ).map(wheel => (winit.name, wheel)))
      )
      .sequence
      .map(_.toMap)

  val symbolsInit: Vector[SymbolsInit] = Vector(
    SymbolsInit("AZ", "ABCDEFGHIJKLMNOPQRSTUVWXYZ")
  )

  def initReflectors(wirings: Wirings): Either[String, Reflectors] = Right(Map.empty)
    // wheelInit
    //   .map( winit =>
    //     wirings
    //       .get(winit.wiringName)
    //       .toRight(s"Wiring '${winit.wiringName}' not defined.")
    //       .flatMap(wiring => Wheel(wiring, winit.notches, SymbolMap.AZ).map(wheel => (winit.name, wheel)))
    //   )
    //   .sequence
    //   .map(_.toMap)

  val wheelInit: Vector[WheelInit] = Vector(
  //          name   wiring  notches ringSetting
    WheelInit("I",    "I",    "Q",  KeyCode.zero),
    WheelInit("II",   "II",   "E",  KeyCode.zero),
    WheelInit("III",  "III",  "V",  KeyCode.zero),
    WheelInit("IV",   "IV",   "J",  KeyCode.zero),
    WheelInit("V",    "V",    "Z",  KeyCode.zero),

    WheelInit("VI",   "VI",   "ZM", KeyCode.zero),
    WheelInit("VII",  "VII",  "ZM", KeyCode.zero),
    WheelInit("VIII", "VIII", "ZM", KeyCode.zero)
  )

  val reflectorInit: Vector[ReflectorInit] = Vector(
    //ReflectorInit("")
  )

  val wiringInit: Vector[WiringInit] = Vector(
    WiringInit("IC",      "AZ",   "DMTWSILRUYQNKFEJCAZBPGXOHV"),
    WiringInit("IIC",     "AZ",   "HQZGPJTMOBLNCIFDYAWVEUSRKX"),
    WiringInit("IIIC",    "AZ",   "UQNTLSZFMREHDPXKIBVYGJCWOA"),
    WiringInit("IR",      "AZ",   "JGDQOXUSCAMIFRVTPNEWKBLZYH"),
    WiringInit("IIR",     "AZ",   "NTZPSFBOKMWRCJDIVLAEYUXHGQ"),
    WiringInit("IIIR",    "AZ",   "JVIUBHTCDYAKEQZPOSGXNRMWFL"),
    WiringInit("UKWR",    "AZ",   "QYHOGNECVPUZTFDJAXWMKISRBL"),
    WiringInit("ETWR",    "AZ",   "QWERTZUIOASDFGHJKPYXCVBNML"),
    WiringInit("IK",      "AZ",   "PEZUOHXSCVFMTBGLRINQJWAYDK"),
    WiringInit("IIK",     "AZ",   "ZOUESYDKFWPCIQXHMVBLGNJRAT"),
    WiringInit("IIIK",    "AZ",   "EHRVXGAOBQUSIMZFLYNWKTPDJC"),
    WiringInit("UKWK",    "AZ",   "IMETCGFRAYSQBZXWLHKDVUPOJN"),
    WiringInit("ETWK",    "AZ",   "QWERTZUIOASDFGHJKPYXCVBNML"),
    WiringInit("I",       "AZ",   "EKMFLGDQVZNTOWYHXUSPAIBRCJ"),
    WiringInit("II",      "AZ",   "AJDKSIRUXBLHWTMCQGZNPYFVOE"),
    WiringInit("III",     "AZ",   "BDFHJLCPRTXVZNYEIWGAKMUSQO"),
    WiringInit("IV",      "AZ",   "ESOVPZJAYQUIRHXLNFTGKDCMWB"),
    WiringInit("V",       "AZ",   "VZBRGITYUPSDNHLXAWMJQOFECK"),
    WiringInit("VI",      "AZ",   "JPGVOUMFYQBENHZRDKASXLICTW"),
    WiringInit("VII",     "AZ",   "NZJHGRCXMYSWBOUFAIVLPEKQDT"),
    WiringInit("VIII",    "AZ",   "FKQHTLXOCBJSPDZRAMEWNIUYGV"),
    WiringInit("BETA",    "AZ",   "LEYJVCNIXWPBQMDRTAKZGFUHOS"),
    WiringInit("GAMMA",   "AZ",   "FSOKANUERHMBTIYCWLQPZXVGJD"),
    WiringInit("A",       "AZ",   "EJMZALYXVBWFCRQUONTSPIKHGD"),
    WiringInit("B",       "AZ",   "YRUHQSLDPXNGOKMIEBFZCWVJAT"),
    WiringInit("C",       "AZ",   "FVPJIAOYEDRZXWGCTKUQSBNMHL"),
    WiringInit("B_THIN",  "AZ",   "ENKQAUYWJICOPBLMDXZVFTHRGS"),
    WiringInit("C_THIN",  "AZ",   "RDOBJNTKVEHMLFCWZAXGYIPSUQ"),
    WiringInit("ETW",     "AZ",   "ABCDEFGHIJKLMNOPQRSTUVWXYZ")
  )

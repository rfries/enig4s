package org.somecode.enig4s
package server
package data

import cats.implicits._
import org.somecode.enigma.mach.Wiring

object WiringRepo:
  def getWiring(name: String): Either[String, Option[Wiring]] = nameMap
    .get(name) match
      //case s @ Some(_) => s.map(mapping => Wiring(mapping)).sequence
      case Some(mapping) => Wiring(mapping).map(Some.apply)
      case None => Right(None)

  def getAllWirings: Map[String, Wiring] = nameMap
    .map((name, mapping) => name -> Wiring(mapping))
    .collect{ case (name, Right(wiring)) => name -> wiring }

  private val nameMap: Map[String,String] = Map(
    "IC"      -> "DMTWSILRUYQNKFEJCAZBPGXOHV",
    "IIC"     -> "HQZGPJTMOBLNCIFDYAWVEUSRKX",
    "IIIC"    -> "UQNTLSZFMREHDPXKIBVYGJCWOA",
    "IR"      -> "JGDQOXUSCAMIFRVTPNEWKBLZYH",
    "IIR"     -> "NTZPSFBOKMWRCJDIVLAEYUXHGQ",
    "IIIR"    -> "JVIUBHTCDYAKEQZPOSGXNRMWFL",
    "UKWR"    -> "QYHOGNECVPUZTFDJAXWMKISRBL",
    "ETWR"    -> "QWERTZUIOASDFGHJKPYXCVBNML",
    "IK"      -> "PEZUOHXSCVFMTBGLRINQJWAYDK",
    "IIK"     -> "ZOUESYDKFWPCIQXHMVBLGNJRAT",
    "IIIK"    -> "EHRVXGAOBQUSIMZFLYNWKTPDJC",
    "UKWK"    -> "IMETCGFRAYSQBZXWLHKDVUPOJN",
    "ETWK"    -> "QWERTZUIOASDFGHJKPYXCVBNML",
    "I"       -> "EKMFLGDQVZNTOWYHXUSPAIBRCJ",
    "II"      -> "AJDKSIRUXBLHWTMCQGZNPYFVOE",
    "III"     -> "BDFHJLCPRTXVZNYEIWGAKMUSQO",
    "IV"      -> "ESOVPZJAYQUIRHXLNFTGKDCMWB",
    "V"       -> "VZBRGITYUPSDNHLXAWMJQOFECK",
    "VI"      -> "JPGVOUMFYQBENHZRDKASXLICTW",
    "VII"     -> "NZJHGRCXMYSWBOUFAIVLPEKQDT",
    "VIII"    -> "FKQHTLXOCBJSPDZRAMEWNIUYGV",
    "BETA"    -> "LEYJVCNIXWPBQMDRTAKZGFUHOS",
    "GAMMA"   -> "FSOKANUERHMBTIYCWLQPZXVGJD",
    "A"       -> "EJMZALYXVBWFCRQUONTSPIKHGD",
    "B"       -> "YRUHQSLDPXNGOKMIEBFZCWVJAT",
    "C"       -> "FVPJIAOYEDRZXWGCTKUQSBNMHL",
    "B_THIN"  -> "ENKQAUYWJICOPBLMDXZVFTHRGS",
    "C_THIN"  -> "RDOBJNTKVEHMLFCWZAXGYIPSUQ",
    "ETW"     -> "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
  )

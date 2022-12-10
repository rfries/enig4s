package org.somecode.enig4s
package mach

object Wirings:
  val chars = SymbolMap.AZ
  val IC:     Wiring =  Wiring(chars.stringToInts("DMTWSILRUYQNKFEJCAZBPGXOHV").require).require
  val IIC:    Wiring =  Wiring(chars.stringToInts("HQZGPJTMOBLNCIFDYAWVEUSRKX").require).require
  val IIIC:   Wiring =  Wiring(chars.stringToInts("UQNTLSZFMREHDPXKIBVYGJCWOA").require).require

  val IR:     Wiring =  Wiring(chars.stringToInts("JGDQOXUSCAMIFRVTPNEWKBLZYH").require).require
  val IIR:    Wiring =  Wiring(chars.stringToInts("NTZPSFBOKMWRCJDIVLAEYUXHGQ").require).require
  val IIIR:   Wiring =  Wiring(chars.stringToInts("JVIUBHTCDYAKEQZPOSGXNRMWFL").require).require
  val UKWR:   Wiring =  Wiring(chars.stringToInts("QYHOGNECVPUZTFDJAXWMKISRBL").require).require
  val ETWR:   Wiring =  Wiring(chars.stringToInts("QWERTZUIOASDFGHJKPYXCVBNML").require).require

  val IK:     Wiring =  Wiring(chars.stringToInts("PEZUOHXSCVFMTBGLRINQJWAYDK").require).require
  val IIK:    Wiring =  Wiring(chars.stringToInts("ZOUESYDKFWPCIQXHMVBLGNJRAT").require).require
  val IIIK:   Wiring =  Wiring(chars.stringToInts("EHRVXGAOBQUSIMZFLYNWKTPDJC").require).require
  val UKWK:   Wiring =  Wiring(chars.stringToInts("IMETCGFRAYSQBZXWLHKDVUPOJN").require).require
  val ETWK:   Wiring =  Wiring(chars.stringToInts("QWERTZUIOASDFGHJKPYXCVBNML").require).require

  val I:      Wiring =  Wiring(chars.stringToInts("EKMFLGDQVZNTOWYHXUSPAIBRCJ").require).require
  val II:     Wiring =  Wiring(chars.stringToInts("AJDKSIRUXBLHWTMCQGZNPYFVOE").require).require
  val III:    Wiring =  Wiring(chars.stringToInts("BDFHJLCPRTXVZNYEIWGAKMUSQO").require).require
  val IV:     Wiring =  Wiring(chars.stringToInts("ESOVPZJAYQUIRHXLNFTGKDCMWB").require).require
  val V:      Wiring =  Wiring(chars.stringToInts("VZBRGITYUPSDNHLXAWMJQOFECK").require).require
  val VI:     Wiring =  Wiring(chars.stringToInts("JPGVOUMFYQBENHZRDKASXLICTW").require).require
  val VII:    Wiring =  Wiring(chars.stringToInts("NZJHGRCXMYSWBOUFAIVLPEKQDT").require).require
  val VIII:   Wiring =  Wiring(chars.stringToInts("FKQHTLXOCBJSPDZRAMEWNIUYGV").require).require

  val BETA:   Wiring =  Wiring(chars.stringToInts("LEYJVCNIXWPBQMDRTAKZGFUHOS").require).require
  val GAMMA:  Wiring =  Wiring(chars.stringToInts("FSOKANUERHMBTIYCWLQPZXVGJD").require).require
  val A:      Wiring =  Wiring(chars.stringToInts("EJMZALYXVBWFCRQUONTSPIKHGD").require).require
  val B:      Wiring =  Wiring(chars.stringToInts("YRUHQSLDPXNGOKMIEBFZCWVJAT").require).require
  val C:      Wiring =  Wiring(chars.stringToInts("FVPJIAOYEDRZXWGCTKUQSBNMHL").require).require
  val B_THIN: Wiring =  Wiring(chars.stringToInts("ENKQAUYWJICOPBLMDXZVFTHRGS").require).require
  val C_THIN: Wiring =  Wiring(chars.stringToInts("RDOBJNTKVEHMLFCWZAXGYIPSUQ").require).require
  val ETW:    Wiring =  Wiring(chars.stringToInts("ABCDEFGHIJKLMNOPQRSTUVWXYZ").require).require

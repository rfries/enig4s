package org.somecode.enig4s
package mach

object Wirings:
  val chars = SymbolMap.AZ
  val IC:     Wiring =  Wiring(chars.stringToGlyphs("DMTWSILRUYQNKFEJCAZBPGXOHV").require).require
  val IIC:    Wiring =  Wiring(chars.stringToGlyphs("HQZGPJTMOBLNCIFDYAWVEUSRKX").require).require
  val IIIC:   Wiring =  Wiring(chars.stringToGlyphs("UQNTLSZFMREHDPXKIBVYGJCWOA").require).require

  val IR:     Wiring =  Wiring(chars.stringToGlyphs("JGDQOXUSCAMIFRVTPNEWKBLZYH").require).require
  val IIR:    Wiring =  Wiring(chars.stringToGlyphs("NTZPSFBOKMWRCJDIVLAEYUXHGQ").require).require
  val IIIR:   Wiring =  Wiring(chars.stringToGlyphs("JVIUBHTCDYAKEQZPOSGXNRMWFL").require).require
  val UKWR:   Wiring =  Wiring(chars.stringToGlyphs("QYHOGNECVPUZTFDJAXWMKISRBL").require).require
  val ETWR:   Wiring =  Wiring(chars.stringToGlyphs("QWERTZUIOASDFGHJKPYXCVBNML").require).require

  val IK:     Wiring =  Wiring(chars.stringToGlyphs("PEZUOHXSCVFMTBGLRINQJWAYDK").require).require
  val IIK:    Wiring =  Wiring(chars.stringToGlyphs("ZOUESYDKFWPCIQXHMVBLGNJRAT").require).require
  val IIIK:   Wiring =  Wiring(chars.stringToGlyphs("EHRVXGAOBQUSIMZFLYNWKTPDJC").require).require
  val UKWK:   Wiring =  Wiring(chars.stringToGlyphs("IMETCGFRAYSQBZXWLHKDVUPOJN").require).require
  val ETWK:   Wiring =  Wiring(chars.stringToGlyphs("QWERTZUIOASDFGHJKPYXCVBNML").require).require

  val I:      Wiring =  Wiring(chars.stringToGlyphs("EKMFLGDQVZNTOWYHXUSPAIBRCJ").require).require
  val II:     Wiring =  Wiring(chars.stringToGlyphs("AJDKSIRUXBLHWTMCQGZNPYFVOE").require).require
  val III:    Wiring =  Wiring(chars.stringToGlyphs("BDFHJLCPRTXVZNYEIWGAKMUSQO").require).require
  val IV:     Wiring =  Wiring(chars.stringToGlyphs("ESOVPZJAYQUIRHXLNFTGKDCMWB").require).require
  val V:      Wiring =  Wiring(chars.stringToGlyphs("VZBRGITYUPSDNHLXAWMJQOFECK").require).require
  val VI:     Wiring =  Wiring(chars.stringToGlyphs("JPGVOUMFYQBENHZRDKASXLICTW").require).require
  val VII:    Wiring =  Wiring(chars.stringToGlyphs("NZJHGRCXMYSWBOUFAIVLPEKQDT").require).require
  val VIII:   Wiring =  Wiring(chars.stringToGlyphs("FKQHTLXOCBJSPDZRAMEWNIUYGV").require).require

  val BETA:   Wiring =  Wiring(chars.stringToGlyphs("LEYJVCNIXWPBQMDRTAKZGFUHOS").require).require
  val GAMMA:  Wiring =  Wiring(chars.stringToGlyphs("FSOKANUERHMBTIYCWLQPZXVGJD").require).require
  val A:      Wiring =  Wiring(chars.stringToGlyphs("EJMZALYXVBWFCRQUONTSPIKHGD").require).require
  val B:      Wiring =  Wiring(chars.stringToGlyphs("YRUHQSLDPXNGOKMIEBFZCWVJAT").require).require
  val C:      Wiring =  Wiring(chars.stringToGlyphs("FVPJIAOYEDRZXWGCTKUQSBNMHL").require).require
  val B_THIN: Wiring =  Wiring(chars.stringToGlyphs("ENKQAUYWJICOPBLMDXZVFTHRGS").require).require
  val C_THIN: Wiring =  Wiring(chars.stringToGlyphs("RDOBJNTKVEHMLFCWZAXGYIPSUQ").require).require
  val ETW:    Wiring =  Wiring(chars.stringToGlyphs("ABCDEFGHIJKLMNOPQRSTUVWXYZ").require).require

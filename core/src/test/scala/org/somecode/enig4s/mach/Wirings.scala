package org.somecode.enig4s
package mach

object Wirings:
  val chars = CharMap.AZ
  val IC:     Wiring =  Wiring(chars.stringToKeyCodes("DMTWSILRUYQNKFEJCAZBPGXOHV").require).require
  val IIC:    Wiring =  Wiring(chars.stringToKeyCodes("HQZGPJTMOBLNCIFDYAWVEUSRKX").require).require
  val IIIC:   Wiring =  Wiring(chars.stringToKeyCodes("UQNTLSZFMREHDPXKIBVYGJCWOA").require).require

  val IR:     Wiring =  Wiring(chars.stringToKeyCodes("JGDQOXUSCAMIFRVTPNEWKBLZYH").require).require
  val IIR:    Wiring =  Wiring(chars.stringToKeyCodes("NTZPSFBOKMWRCJDIVLAEYUXHGQ").require).require
  val IIIR:   Wiring =  Wiring(chars.stringToKeyCodes("JVIUBHTCDYAKEQZPOSGXNRMWFL").require).require
  val UKWR:   Wiring =  Wiring(chars.stringToKeyCodes("QYHOGNECVPUZTFDJAXWMKISRBL").require).require
  val ETWR:   Wiring =  Wiring(chars.stringToKeyCodes("QWERTZUIOASDFGHJKPYXCVBNML").require).require

  val IK:     Wiring =  Wiring(chars.stringToKeyCodes("PEZUOHXSCVFMTBGLRINQJWAYDK").require).require
  val IIK:    Wiring =  Wiring(chars.stringToKeyCodes("ZOUESYDKFWPCIQXHMVBLGNJRAT").require).require
  val IIIK:   Wiring =  Wiring(chars.stringToKeyCodes("EHRVXGAOBQUSIMZFLYNWKTPDJC").require).require
  val UKWK:   Wiring =  Wiring(chars.stringToKeyCodes("IMETCGFRAYSQBZXWLHKDVUPOJN").require).require
  val ETWK:   Wiring =  Wiring(chars.stringToKeyCodes("QWERTZUIOASDFGHJKPYXCVBNML").require).require

  val I:      Wiring =  Wiring(chars.stringToKeyCodes("EKMFLGDQVZNTOWYHXUSPAIBRCJ").require).require
  val II:     Wiring =  Wiring(chars.stringToKeyCodes("AJDKSIRUXBLHWTMCQGZNPYFVOE").require).require
  val III:    Wiring =  Wiring(chars.stringToKeyCodes("BDFHJLCPRTXVZNYEIWGAKMUSQO").require).require
  val IV:     Wiring =  Wiring(chars.stringToKeyCodes("ESOVPZJAYQUIRHXLNFTGKDCMWB").require).require
  val V:      Wiring =  Wiring(chars.stringToKeyCodes("VZBRGITYUPSDNHLXAWMJQOFECK").require).require
  val VI:     Wiring =  Wiring(chars.stringToKeyCodes("JPGVOUMFYQBENHZRDKASXLICTW").require).require
  val VII:    Wiring =  Wiring(chars.stringToKeyCodes("NZJHGRCXMYSWBOUFAIVLPEKQDT").require).require
  val VIII:   Wiring =  Wiring(chars.stringToKeyCodes("FKQHTLXOCBJSPDZRAMEWNIUYGV").require).require

  val BETA:   Wiring =  Wiring(chars.stringToKeyCodes("LEYJVCNIXWPBQMDRTAKZGFUHOS").require).require
  val GAMMA:  Wiring =  Wiring(chars.stringToKeyCodes("FSOKANUERHMBTIYCWLQPZXVGJD").require).require
  val A:      Wiring =  Wiring(chars.stringToKeyCodes("EJMZALYXVBWFCRQUONTSPIKHGD").require).require
  val B:      Wiring =  Wiring(chars.stringToKeyCodes("YRUHQSLDPXNGOKMIEBFZCWVJAT").require).require
  val C:      Wiring =  Wiring(chars.stringToKeyCodes("FVPJIAOYEDRZXWGCTKUQSBNMHL").require).require
  val B_THIN: Wiring =  Wiring(chars.stringToKeyCodes("ENKQAUYWJICOPBLMDXZVFTHRGS").require).require
  val C_THIN: Wiring =  Wiring(chars.stringToKeyCodes("RDOBJNTKVEHMLFCWZAXGYIPSUQ").require).require
  val ETW:    Wiring =  Wiring(chars.stringToKeyCodes("ABCDEFGHIJKLMNOPQRSTUVWXYZ").require).require

package org.somecode.enig4s
package mach

object Wirings:
  val chars = SymbolMap.AZ
  val IC:     Wiring =  Wiring(chars.stringToCodes("DMTWSILRUYQNKFEJCAZBPGXOHV").require).require
  val IIC:    Wiring =  Wiring(chars.stringToCodes("HQZGPJTMOBLNCIFDYAWVEUSRKX").require).require
  val IIIC:   Wiring =  Wiring(chars.stringToCodes("UQNTLSZFMREHDPXKIBVYGJCWOA").require).require

  val IR:     Wiring =  Wiring(chars.stringToCodes("JGDQOXUSCAMIFRVTPNEWKBLZYH").require).require
  val IIR:    Wiring =  Wiring(chars.stringToCodes("NTZPSFBOKMWRCJDIVLAEYUXHGQ").require).require
  val IIIR:   Wiring =  Wiring(chars.stringToCodes("JVIUBHTCDYAKEQZPOSGXNRMWFL").require).require
  val UKWR:   Wiring =  Wiring(chars.stringToCodes("QYHOGNECVPUZTFDJAXWMKISRBL").require).require
  val ETWR:   Wiring =  Wiring(chars.stringToCodes("QWERTZUIOASDFGHJKPYXCVBNML").require).require

  val IK:     Wiring =  Wiring(chars.stringToCodes("PEZUOHXSCVFMTBGLRINQJWAYDK").require).require
  val IIK:    Wiring =  Wiring(chars.stringToCodes("ZOUESYDKFWPCIQXHMVBLGNJRAT").require).require
  val IIIK:   Wiring =  Wiring(chars.stringToCodes("EHRVXGAOBQUSIMZFLYNWKTPDJC").require).require
  val UKWK:   Wiring =  Wiring(chars.stringToCodes("IMETCGFRAYSQBZXWLHKDVUPOJN").require).require
  val ETWK:   Wiring =  Wiring(chars.stringToCodes("QWERTZUIOASDFGHJKPYXCVBNML").require).require

  val I:      Wiring =  Wiring(chars.stringToCodes("EKMFLGDQVZNTOWYHXUSPAIBRCJ").require).require
  val II:     Wiring =  Wiring(chars.stringToCodes("AJDKSIRUXBLHWTMCQGZNPYFVOE").require).require
  val III:    Wiring =  Wiring(chars.stringToCodes("BDFHJLCPRTXVZNYEIWGAKMUSQO").require).require
  val IV:     Wiring =  Wiring(chars.stringToCodes("ESOVPZJAYQUIRHXLNFTGKDCMWB").require).require
  val V:      Wiring =  Wiring(chars.stringToCodes("VZBRGITYUPSDNHLXAWMJQOFECK").require).require
  val VI:     Wiring =  Wiring(chars.stringToCodes("JPGVOUMFYQBENHZRDKASXLICTW").require).require
  val VII:    Wiring =  Wiring(chars.stringToCodes("NZJHGRCXMYSWBOUFAIVLPEKQDT").require).require
  val VIII:   Wiring =  Wiring(chars.stringToCodes("FKQHTLXOCBJSPDZRAMEWNIUYGV").require).require

  val BETA:   Wiring =  Wiring(chars.stringToCodes("LEYJVCNIXWPBQMDRTAKZGFUHOS").require).require
  val GAMMA:  Wiring =  Wiring(chars.stringToCodes("FSOKANUERHMBTIYCWLQPZXVGJD").require).require
  val A:      Wiring =  Wiring(chars.stringToCodes("EJMZALYXVBWFCRQUONTSPIKHGD").require).require
  val B:      Wiring =  Wiring(chars.stringToCodes("YRUHQSLDPXNGOKMIEBFZCWVJAT").require).require
  val C:      Wiring =  Wiring(chars.stringToCodes("FVPJIAOYEDRZXWGCTKUQSBNMHL").require).require
  val B_THIN: Wiring =  Wiring(chars.stringToCodes("ENKQAUYWJICOPBLMDXZVFTHRGS").require).require
  val C_THIN: Wiring =  Wiring(chars.stringToCodes("RDOBJNTKVEHMLFCWZAXGYIPSUQ").require).require
  val ETW:    Wiring =  Wiring(chars.stringToCodes("ABCDEFGHIJKLMNOPQRSTUVWXYZ").require).require

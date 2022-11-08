package org.somecode.enig4s.mach

/**
  * Represents the modulus, or the value at which increasing values wrap
  * around to zero. This, in effect, defines the domain of the transposition
  * functions of the various components in the [[Machine]], and of the modular
  * operations they are built on.
  */
opaque type Modulus = Int
object Modulus extends NewPosNum[Modulus, Int]:
  extension (mod: Modulus)
    def toInt: Int = mod

  // def apply(n: Int): Either[String, BusSize] =
  //   if n <= 0 then
  //     Left(s"Bus size ($n) must be greater than zero.")
  //   else
  //     Right(n)

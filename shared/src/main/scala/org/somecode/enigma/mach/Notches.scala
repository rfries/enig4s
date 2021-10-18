package org.somecode.enigma.mach

final case class Notches(notches: Set[KeyCode])

object Notches:
  def apply(notchStrings: Set[String]): Either[String, Notches] =
    if (notchStrings.exists(_.size != 1))
      Left("All notch strings must have a length of 1.")
    else
      val notchCodes = notchStrings.map(s => s(0) - 'A')
      if (notchCodes.exists(n => n < 0 || n >= 26))
        Left("Notch specifications must be between 'A' and 'Z'.")
      else
        Right(Notches(notchCodes.map(KeyCode.unsafe)))

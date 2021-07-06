package org.somecode.enigma
package mach

sealed abstract class ValidKeys private (max: Int, codes: Vector[KeyCode])

object ValidKeys:
  def apply(max: Int, codes: Vector[KeyCode]): Either[String, ValidKeys] =
    if (max < 1)
      Left("ValidKeys max ($max) must be positive.")
    else if (codes.exists(k => k.toInt < 0 || k.toInt >= max))
      Left(s"All KeyCodes must be between 0 and ${max-1}.")
    else
      Right(new ValidKeys(max, codes) {})

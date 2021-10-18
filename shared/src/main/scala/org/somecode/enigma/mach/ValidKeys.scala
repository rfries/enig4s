package org.somecode.enigma
package mach

sealed abstract case class ValidKeys private (numCodes: Int, codes: Vector[KeyCode]):
  override def toString: String = String(codes.map(k => (k + 'A').toChar).toArray)

object ValidKeys:
  // Currently supports only 26 letters (upper case ASCII)
  val NumCodesBasic = 26

  def apply(numCodes: Int, text: Vector[KeyCode]): Either[String, ValidKeys] =
    if (numCodes < 1)
      Left(s"ValidKeys numCodes ($numCodes) must be positive.")
    else if (text.exists(k => k < 0 || k >= numCodes))
      Left(s"All KeyCodes must be between 0 and ${numCodes-1}.")
    else
      Right(new ValidKeys(numCodes, text) {})

  def apply(text: String): Either[String, ValidKeys] =
    val uppered = text.toUpperCase
    if (uppered.exists(c => c < 'A'|| c > 'Z'))
      Left("All characters must be between A and Z.")
    else
      Right(new ValidKeys(NumCodesBasic, uppered.map(c => KeyCode.apply((c - 'A').toChar)).toVector) {})
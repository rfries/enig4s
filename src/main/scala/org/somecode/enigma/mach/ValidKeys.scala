package org.somecode.enigma
package mach

sealed abstract case class ValidKeys private (numCodes: Int, codes: Vector[KeyCode]):
  override def toString: String = String(codes.map(_.toChar).toArray)

object ValidKeys:
  // Currently supports only 26 letters (upper case ASCII)
  val NumCodesBasic = 26

  def apply(numCodes: Int, text: Vector[Char]): Either[String, ValidKeys] =
    if (numCodes < 1)
      Left("ValidKeys max ($max) must be positive.")
    else if (text.exists(k => k < 0 || k >= numCodes))
      Left(s"All KeyCodes must be between 0 and ${numCodes-1}.")
    else
      Right(new ValidKeys(numCodes, text.map(KeyCode.unsafe)) {})
  
  def apply(text: String): Either[String, ValidKeys] =
    apply(NumCodesBasic, text.toVector)

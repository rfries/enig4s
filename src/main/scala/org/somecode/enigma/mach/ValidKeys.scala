package org.somecode.enigma
package mach

sealed abstract case class ValidKeys private (numCodes: Int, codes: Vector[KeyCode]):
  override def toString: String = String(codes.map(_.toChar).toArray)

object ValidKeys:
  // Currently supports only 26 letters (upper case ASCII)
  val NumCodesBasic = 26

  def apply(numCodes: Int, text: Vector[KeyCode]): Either[String, ValidKeys] =
    if (numCodes < 1)
      Left("ValidKeys max ($max) must be positive.")
    else if (text.exists(k => k < 0 || k >= numCodes))
      Left(s"All KeyCodes must be between 0 and ${numCodes-1}.")
    else
      Right(new ValidKeys(numCodes, text) {})

  // def apply(numCodes: Int, text: Vector[Char]): Either[String, ValidKeys] =
  //   apply(numCodes, text.map(_.toChar))

  def apply(text: String): Either[String, ValidKeys] =
    if (text.exists(_ >= NumCodesBasic))
      Left(s"All characters must be between 0 and ${NumCodesBasic-1}.")
    else
      Right(new ValidKeys(NumCodesBasic, text.map(KeyCode.unsafe).toVector) {})
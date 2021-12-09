package org.somecode.enig4s
package mach

import Machine.Bus

sealed abstract case class Wiring private (
  forward: Vector[KeyCode],
  reverse: Vector[KeyCode]
) extends Bus:

  def size: Int = forward.size

  override def translate(in: KeyCode): KeyCode =
    val out = forward(in.toInt)
    //println(f"w:      $in%02d (${(in + 'A').toChar}) -> $out%02d (${(out + 'A').toChar})")
    out

  override def reverseTranslate(in: KeyCode): KeyCode =
    val out = reverse(in.toInt)
    //println(f"w:      $out%02d (${(out + 'A').toChar}) <- $in%02d (${(in + 'A').toChar})")
    out

end Wiring

object Wiring:

  /** Create Wiring from a vector of key codes  */
  def apply(keyCodes: Vector[KeyCode]): Either[String, Wiring] = keyCodes match
    case v if v.length <= 0 => Left("Wiring vectors have at least one entry.")
    case v if v.length != v.distinct.length => Left(s"Wiring vectors must not contain duplicate values.")
    case v if v.exists(n => n < 0 || n >= v.length) => Left(s"Wiring vectors must contain only values from 0 (inclusive) to ${v.length} (exclusive).")
    case v => verifyContinuity(v)

  private def verifyContinuity(v: Vector[KeyCode]): Either[String, Wiring] =
    val rev = v.zipWithIndex.sortBy(_._1)
    val diffsz = rev(v.size-1)._1 - rev(0)._1 + 1
    if (diffsz != v.size)
      Left("Wiring vector must be continuous.")
    else
      Right(new Wiring(v, rev.map((_, idx) => KeyCode.unsafe(idx))) {})

end Wiring

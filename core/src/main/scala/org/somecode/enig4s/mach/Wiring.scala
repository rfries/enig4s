package org.somecode.enig4s
package mach

import org.somecode.enig4s.mach.Machine.Bus

import scala.collection.immutable.ArraySeq

sealed abstract case class Wiring private (forward: IndexedSeq[KeyCode])
  extends Bus:

  val reverse: IndexedSeq[KeyCode] = forward
    .zipWithIndex
    .sortBy(_._1)
    .map((_, idx) => KeyCode.unsafe(idx))
    .to(ArraySeq)

  def size: Int = forward.size

  override def translate(in: KeyCode): KeyCode =
    val out = forward(in)
    //println(f"w:      $in%02d (${(in + 'A').toChar}) -> $out%02d (${(out + 'A').toChar})")
    out

  override def reverseTranslate(in: KeyCode): KeyCode =
    val out = reverse(in)
    //println(f"w:      $out%02d (${(out + 'A').toChar}) <- $in%02d (${(in + 'A').toChar})")
    out

end Wiring

object Wiring:

  /** Create Wiring from a vector of key codes  */
  def apply(keyCodes: IndexedSeq[KeyCode]): Either[String, Wiring] = keyCodes match
    case v if v.isEmpty => Left("Wiring vectors have at least one entry.")
    case v if v.length != v.distinct.length => Left(s"Wiring vectors must not contain duplicate values.")
    case v if v.exists(_ >= v.length) => Left(s"Wiring vectors must contain only values from 0 (inclusive) to ${v.length} (exclusive).")
    case v => Right(new Wiring(v) {})

  val AZ: Wiring = Wiring((0 to 25).map(KeyCode.unsafe))
    .getOrElse(throw RuntimeException("Wiring: Bad Init"))
end Wiring

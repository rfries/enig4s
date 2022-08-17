package org.somecode.enig4s
package mach

import scala.collection.immutable.ArraySeq

sealed abstract case class Wiring private (codes: IndexedSeq[KeyCode]):
  val size: Int = codes.size
  val forward: KeyCode => KeyCode = in => codes(in)
  val reverse: KeyCode => KeyCode = in => reverseCodes(in)

  private[mach] lazy val reverseCodes: IndexedSeq[KeyCode] = codes
    .zipWithIndex
    .sortBy(_._1)
    .map((_, idx) => KeyCode.unsafe(idx))
    .to(ArraySeq)

object Wiring:

  /** Create Wiring from a vector of key codes  */
  def apply(keyCodes: IndexedSeq[KeyCode]): Either[String, Wiring] = keyCodes match
    case v if v.isEmpty =>
      Left("Wiring vectors have at least one value.")
    case v if v.length != v.distinct.length =>
      Left(s"Wiring vectors must not contain duplicate values.")
    case v if v.exists(_ >= v.length) =>
      Left(s"Wiring vectors must contain only values from 0 (inclusive) to ${v.length} (exclusive).")
    case v => Right(new Wiring(v) {})

  def apply(mapping: String, symbols: SymbolMap): Either[String, Wiring] =
    symbols.stringToCodes(mapping).flatMap(Wiring.apply)

  // A straight-through mapping, used by the keyboard on most models
  val AZ: Wiring = Wiring((0 to 25).map(KeyCode.unsafe))
    .getOrElse(throw RuntimeException("Wiring: Bad Init"))

end Wiring

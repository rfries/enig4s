package org.somecode.enig4s
package mach

import cats.*
import cats.implicits.*

/**
 * Convert external representations (String and Unicode Code Points)
 * to and from [[KeyCode]]s.
 */
class SymbolMap private(codeToPoint: IndexedSeq[KeyCode]):

  val pointToCode: Map[Int, KeyCode] = codeToPoint.zipWithIndex.map(_.swap).toMap
  val size: Int = codeToPoint.size

//  val forward: Map[Int, KeyCode] = mapping
//    .codePoints
//    .toArray
//    .zipWithIndex
//    .map { (c, idx) => c -> KeyCode.unsafe(idx) }
//    .toMap


  def pointToCode(cp: Int): Either[String, KeyCode] =
    pointToCode.get(cp).toRight(f"Code point $cp%x not found in map.")

  def codeToPoint(key: KeyCode): Either[String, Int] =
    codeToPoint.lift(key).toRight(s"Key code $key not found in map.")

  def stringToKeyCodes(in: String): Either[String, IndexedSeq[KeyCode]] =
    in.codePoints.toArray.map(pointToCode.get).toVector.sequence match {
      case Some(out) => Right(out)
      case None =>
        val bad = in.filterNot(codeToPoint.isDefinedAt).map(c => f"'$c%c' (${c.toInt}%#04x)").mkString(",")
        Left(s"Invalid character(s) for character map: $bad")
    }

  def keyCodesToString(in: IndexedSeq[KeyCode]): Either[String, String] =
    in.map(codeToPoint.lift).toVector.sequence match {
      case Some(out) => Right(String(out.toArray[Int], 0, out.length))
      case None => Left("Key code not found in character map.")
    }

end SymbolMap

object SymbolMap:

  def apply(codes: IndexedSeq[KeyCode]): Either[String, SymbolMap] =
    if (codes.length != codes.distinct.length)
      Left("Symbol maps must not contain duplicate values.")
    else
      Right(new SymbolMap(codes))

  def apply(mapping: String): Either[String, SymbolMap] =
    apply(mapping.codePoints.toArray.map(KeyCode.unsafe))

  val AZ: SymbolMap = SymbolMap("ABCDEFGHIJKLMNOPQRSTUVWXYZ")
    .getOrElse(throw RuntimeException("Symbol Map: Bad Init"))

end SymbolMap

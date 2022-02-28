package org.somecode.enig4s
package mach

import cats.*
import cats.implicits.*

import scala.collection.immutable.ArraySeq

/**
 * Convert external representations (String and Unicode Code Points)
 * to and from [[KeyCode]]s.
 */
class SymbolMap private(codePoints: IndexedSeq[KeyCode]):

  val pointToCode: Map[Int, KeyCode] = codePoints.zipWithIndex
    .map((pt, code) => (pt: Int, KeyCode.unsafe(code)))
    .toMap

  val size: Int = codePoints.size

  def pointToCode(cp: Int): Either[String, KeyCode] =
    pointToCode.get(cp).toRight(f"Code point $cp%x not found in map.")

  def codeToPoint(key: KeyCode): Either[String, Int] =
    codePoints.lift(key).toRight(s"Key code $key not found in map.")

  def stringToKeyCodes(in: String): Either[String, IndexedSeq[KeyCode]] =
    in.codePoints.toArray.to(ArraySeq).map(pointToCode.get).sequence match {
      case Some(out) => Right(out)
      case None =>
        val bad = in.filterNot(codePoints.isDefinedAt).map(c => f"'$c%c' (${c.toInt}%#04x)").mkString(",")
        Left(s"Invalid character(s) for character map: $bad")
    }

  def keyCodesToString(in: IndexedSeq[KeyCode]): Either[String, String] =
    in.map(codePoints.lift).to(ArraySeq).sequence match {
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
    // all code points are valid KeyCodes, so unsafe is relatively safe here
    apply(mapping.codePoints.toArray.map(KeyCode.unsafe).to(ArraySeq))

  val AZ: SymbolMap = SymbolMap("ABCDEFGHIJKLMNOPQRSTUVWXYZ")
    .getOrElse(throw RuntimeException("Symbol Map: Bad Init"))

end SymbolMap

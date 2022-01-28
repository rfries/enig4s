package org.somecode.enig4s
package mach

import cats.*
import cats.implicits.*

/**
 * Convert external representations (String and Unicode Code Points)
 * to and from [[KeyCode]]s.
 */
class CharMap private (mapping: String):

  val forward: Map[Int, KeyCode] = mapping
    .codePoints
    .toArray
    .zipWithIndex
    .map { (c, idx) => c -> KeyCode.unsafe(idx) }
    .toMap

  val reversed: Map[KeyCode, Int] = forward.map(_.swap)

  def size: Int = forward.size

  def codePointToKeyCode(cp: Int): Either[String, KeyCode] =
    forward.get(cp).toRight(f"Code point $cp%x not found in map.")

  def keyCodeToCodePoint(key: KeyCode): Either[String, Int] =
    reversed.get(key).toRight(s"Key code $key not found in map.")

  def stringToKeyCodes(in: String): Either[String, Vector[KeyCode]] =
    in.codePoints.toArray.map(forward.get).toVector.sequence match {
      case Some(out) => Right(out)
      case None =>
        val bad = in.filterNot(forward.isDefinedAt).map(c => f"'$c%c' (${c.toInt}%#04x)").mkString(",")
        Left(s"Invalid character(s) for character map: $bad")
    }

  def keyCodesToString(in: Vector[KeyCode]): Either[String, String] =
    in.map(reversed.get).sequence match {
      case Some(out) => Right(String(out.toArray, 0, out.length))
      case None => Left("Key code not found in character map.")
    }

end CharMap

object CharMap:

  def apply(mapping: String): Either[String, CharMap] =
    if (mapping.length != mapping.distinct.length)
      Left("Character maps must not contain duplicate values.")
    else
      Right(new CharMap(mapping))

  val AZ: CharMap = CharMap("ABCDEFGHIJKLMNOPQRSTUVWXYZ")
    .getOrElse(throw RuntimeException("Character Map: Bad Init"))

end CharMap

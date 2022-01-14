package org.somecode.enig4s
package mach

import cats.*
import cats.implicits.*

class CharMap private (mapping: String):

  val forward: Map[Char, KeyCode] = mapping
    .zipWithIndex
    .map { case (c, idx) => c -> KeyCode.unsafe(idx) }
    .toMap

  val reversed: Map[KeyCode, Char] = forward.map(_.swap).toMap

  def size: Int = forward.size

  def stringToKeyCodes(in: String): Either[String, Vector[KeyCode]] =
    in.map(forward.get).toVector.sequence match {
      case Some(out) => Right(out)
      case None =>
        val bad = in.filterNot(forward.isDefinedAt).map(c => f"'$c%c' (${c.toInt}%#04x)").mkString(",")
        Left(s"Invalid character(s) for character map: $bad")
    }

  def keyCodesToString(in: Vector[KeyCode]): Either[String, String] =
    in.map(reversed.get).sequence match {
      case Some(out) => Right(String(out.toArray))
      case None => Left("Key code not found in character map.")
    }

end CharMap

object CharMap:

  def apply(mapping: String): Either[String, CharMap] =
    if (mapping.size != mapping.distinct.size)
      Left("Character maps must not contain duplicate values.")
    else
      Right(new CharMap(mapping))

  val AZ: CharMap = CharMap("ABCDEFGHIJKLMNOPQRSTUVWXYZ")
    .getOrElse(throw RuntimeException("Character Map: Bad Init"));

end CharMap

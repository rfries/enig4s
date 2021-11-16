package org.somecode.enig4s
package mach

import cats.*
import cats.implicits.*

class CharacterMap private (mapping: String):

  val forward: Map[Char, KeyCode] = mapping
    .zipWithIndex
    .map { case (c, idx) => c -> KeyCode.unsafe(idx) }
    .toMap

  val reversed: Map[KeyCode, Char] = forward.map(_.swap).toMap

  def size: Int = forward.size

  def stringtoKeyCodes(in: String): Either[String, Vector[KeyCode]] =
    in.map(forward.get).toVector.sequence match {
      case Some(out) => Right(out)
      case None => Left("CharacterMap: invalid character in input.")
    }

  def keyCodesToString(in: Vector[KeyCode]): Either[String, String] =
    in.map(reversed.get).sequence match {
      case Some(out) => Right(String(out.toArray))
      case None => Left("Key code not found in character map.")
    }

object CharacterMap:
  def apply(mapping: String): Either[String, CharacterMap] =
    if mapping.size != mapping.distinct.size then
      Left("Character maps must not contain duplicate values.")
    else
      Right(new CharacterMap(mapping))

package org.somecode.enig4s
package mach

import cats.*
import cats.implicits.*

import scala.collection.immutable.ArraySeq

trait PlugBoard:
  def size: Int

  val maxPlugs: Int = size / 2

  def forward: KeyCode => KeyCode
  def reverse: KeyCode => KeyCode

/**
  * Represents an Enigma-style plugboard, in which each patch represents two
  * characters which are swapped.
  *
  * @param size   bus size
  * @param map    set of character to character mappings which represent the
  *               patch pairs
  */
sealed abstract case class EnigmaPlugBoard private (size: Int, mapping: Map[KeyCode, KeyCode])
  extends PlugBoard:

  // for the enigma plugboard, reciprocal mappings are always added, so forward/reverse is the same operation

  def forward: KeyCode => KeyCode = key => mapping.getOrElse(key, key)
  def reverse: KeyCode => KeyCode = forward

object EnigmaPlugBoard:

  def apply(size: Int, pairings: IndexedSeq[(KeyCode, KeyCode)]): Either[String, EnigmaPlugBoard] =
    if (size < 1)
      Left("The plugboard size must be greater than 0")
    else if (pairings.size > size / 2)
      Left(s"This plugboard can have no more than ${size/2} pairings.")
    else
      val allKeys: IndexedSeq[KeyCode] = pairings.flatMap(tup => Vector(tup._1, tup._2))
      if (allKeys.contains((k: KeyCode) => k >= size))
        Left(s"Key codes for this plugboard must be < $size")
      // this check also covers pairings that map to themselves (since that requires duplicates)
      else if (allKeys.size != allKeys.distinct.size)
        Left("Key codes cannot be duplicated in either source or target of pairings.")
      else
        Right(new EnigmaPlugBoard(size, pairings.toMap ++ pairings.map(_.swap).toMap) {})

  def apply(size: Int, pairings: IndexedSeq[String], symbols: SymbolMap): Either[String, PlugBoard] =
    if pairings.exists(_.length != 2) then
      Left("Pairing strings must contain only two symbols per string.")
    else
      for
        codes <- pairings.to(ArraySeq).traverse(p => symbols.stringToCodes(p).map(v => v(0) -> v(1)))
        pboard <- apply(size, codes)
      yield pboard


//    Either.cond(size > 0, new EnigmaPlugBoard(size) {}, "PlugBoard size must be greater than zero")

sealed abstract case class TypeXPlugBoard private (wiring: Wiring)
  extends PlugBoard:

  val size = wiring.size

  def forward: KeyCode => KeyCode = wiring.forward
  def reverse: KeyCode => KeyCode = wiring.reverse

object TypeXPlugBoard:

  def apply(size: Int, wiring: Wiring): Either[String, TypeXPlugBoard] =
    Either.cond(
      size === wiring.size,
      new TypeXPlugBoard(wiring) {},
      s"PlugBoard size (${wiring.size}) must match bus size ($size)"
    )

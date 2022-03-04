package org.somecode.enig4s
package mach

import cats.*
import cats.implicits.*

import scala.collection.immutable.ArraySeq

/**
  * Represents an Enigma-style plugboard, in which each patch represents two
  * characters which are swapped.
  *
  * @param size   bus size
  * @param map    set of character to character mappings which represent the
  *               patch pairs
  */
final case class PlugBoard private (size: Int, map: Map[KeyCode, KeyCode]):

  val maxPlugs: Int = size / 2

  def translate(in: KeyCode): KeyCode =
    val out = map.getOrElse(in, in)
    println(f"p:      $in%02d (${(in + 'A').toChar}) -> $out%02d (${(out + 'A').toChar})")
    out

  /**
   * This method is only distinct from translate for debugging and tracing (it is functionally identical.
   */
  def reverseTranslate(in: KeyCode): KeyCode =
    val out = map.getOrElse(in, in)
    println(f"p:      $out%02d (${(out + 'A').toChar}) <- $in%02d (${(in + 'A').toChar})")
    out

object PlugBoard:

  def empty(sz: Int): PlugBoard = new PlugBoard(sz, Map.empty)

  def apply(size: Int, pairings: IndexedSeq[String], symbols: SymbolMap): Either[String, PlugBoard] =
    if pairings.exists(_.length != 2) then
      Left("Pairing strings must contain only two symbols per string.")
    else
      for
        codes <- pairings.map(p => symbols.stringToCodes(p).map(v => v(0) -> v(1)))
          .to(ArraySeq)
          .sequence
        sz <- validSize(size)
        pboard <- apply(sz, codes)
      yield pboard

  def apply(size: Int, pairings: IndexedSeq[(KeyCode, KeyCode)]): Either[String, PlugBoard] =
    if pairings.size > size / 2 then
      Left(s"This plugboard can have no more than ${size/2} pairings.")
    else
      val allKeys: IndexedSeq[KeyCode] = pairings.flatMap(tup => Vector(tup._1, tup._2))
      if allKeys.contains((k: KeyCode) => k >= size) then
        Left(s"Key codes for this plugboard must be < $size")
      // this check also covers pairings that map to themselves (since that requires duplicates)
      else if allKeys.size != allKeys.distinct.size then
        Left("Key codes cannot be duplicated in either source or target of pairings.")
      else if pairings.exists((k, v) => k == v) then
        Left("Pairings cannot map to themselves.")
      else
        Right(new PlugBoard(size, pairings.toMap ++ pairings.map(_.swap).toMap))

  private def validSize(size: Int): Either[String, Int] =
    if size < 1 then
      Left(s"Plugboard size ($size) must be greater than 0.")
    else
      Right(size)

end PlugBoard

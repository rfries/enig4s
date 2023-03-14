package org.somecode.enig4s
package mach

import cats.data.Writer
import cats.*
import cats.implicits.*
import Trace.*

import scala.collection.immutable.ArraySeq

trait PlugBoard extends Bidirectional:
  def length: Int

  def forward: Transformer
  def reverse: Transformer

/**
  * Represents an Enigma-style (reciprocal) plugboard, in which each patch
  * represents two glyphs which are swapped, resulting in the same mappings
  * in both directions.
  *
  * @param length bus size
  * @param map    set of character to character mappings which represent the
  *               patch pairs
  */
sealed abstract case class EnigmaPlugBoard private (length: Int, mapping: Map[Glyph, Glyph])
  extends PlugBoard:

  def forward: Transformer = (state, glyph) =>
    val out = mapping.getOrElse(glyph, glyph)
    Writer(Trace.trace(state, glyph, out, Component.Plugboard(Direction.Forward)), out)

  // for the enigma plugboard, reciprocal mappings are always added, so forward/reverse is the same operation
  def reverse: Transformer = (state, glyph) =>
    val out = mapping.getOrElse(glyph, glyph)
    Writer(Trace.trace(state, glyph, out, Component.Plugboard(Direction.Reverse)), out)

object EnigmaPlugBoard:

  def apply(size: Int, pairings: IndexedSeq[(Glyph, Glyph)]): Either[String, EnigmaPlugBoard] =
    if (size < 1)
      Left("The plugboard size must be greater than 0")
    else if (pairings.size > size / 2)
      Left(s"This plugboard can have no more than ${size/2} pairings.")
    else
      val allKeys: IndexedSeq[Glyph] = pairings.flatMap(tup => Vector(tup._1, tup._2))
      if (allKeys.contains((g: Glyph) => g.toInt >= size))
        Left(s"Glyphs for this plugboard must be < $size")
      // this check also covers pairings that map to themselves (since that requires duplicates)
      else if (allKeys.size != allKeys.distinct.size)
        Left("Glyphs cannot be duplicated in either source or target of pairings.")
      else
        Right(new EnigmaPlugBoard(size, pairings.toMap ++ pairings.map(_.swap).toMap) {})

  def apply(size: Int, pairings: IndexedSeq[String], symbols: SymbolMap): Either[String, EnigmaPlugBoard] =
    if pairings.exists(_.length != 2) then
      Left("Pairing strings must contain only two symbols per string.")
    else
      for
        codes <- pairings.to(ArraySeq).traverse(p => symbols.stringToGlyphs(p).map(v => v(0) -> v(1)))
        pboard <- apply(size, codes)
      yield pboard

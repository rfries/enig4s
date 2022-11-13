package org.somecode.enig4s
package mach

import cats.implicits.*
import cats.syntax.all.*
import scala.collection.immutable.ArraySeq

final case class Wiring private (wiring: ArraySeq[Glyph]) extends Transformer:

  val length: Int = wiring.length

  // provide a modulus based on the 'wires' array size, (already validated as non-zero)
  given modulus: Modulus = Modulus.unsafe(length)

  override val transformer: (MachineState, Glyph) => (MachineState, Glyph) =
    (state, in) => (state, wire(in))

  /**
    * Look up a glyph in the wiring table.  Unsafe; this is just an array
    * look up that takes a Glyph instead of an Int.
    *
    * @param g The glyph to look up.
    * @return The value (the "other end" of the wire)
    */
  inline def wire(g: Glyph) = wiring(g.toInt)

  /**
   * Get a [[Wiring]] with an inverse mapping.
   * To get the inverse, we zip with the index, then sort the resulting
   * tuples by the original values, which are then discarded. The resulting
   * sorted indices represent the inverse mapping. For instance,
   * if `glyphs(3) === 22`, then `inverse.glyphs(22) === 3`
   *
   * Note that since we know that the index is >= 0, and <= the size of the mapping
   * array, calling 'Glyph.unsafe` for each index value should be safe.
   */
  def inverse: Wiring = Wiring(wiring.zipWithIndex
    .sortBy(_._1)
    .map((_, idx) => Glyph.unsafe(idx))
  )

object Wiring:

  /** Create Wiring from a vector of unicode code points   */
  def apply(points: Seq[Int]): Either[String, Wiring] = points match
    case v if v.isEmpty =>
      Left("Wiring vectors must have at least one value.")
    case v if v.length != v.distinct.length =>
      Left(s"Wiring vectors must not contain duplicate values.")
    case v if v.exists(c => c < 0 || c >= v.length) =>
      Left(s"Wiring vector must contain only values from 0 (inclusive) to the vector length ${v.length} (exclusive).")
    case v => Right(Wiring(v.map(Glyph.unsafe).to(ArraySeq)))

  def apply(mapping: String, symbols: SymbolMap): Either[String, Wiring] =
    symbols.stringToGlyphs(mapping).flatMap(gs => Wiring(gs.map(_.toInt)))

  // A straight-through mapping, used by the keyboard on most models
  val AZ: Wiring = Wiring((0 to 25).map(KeyCode.unsafe).to(ArraySeq))
    .getOrElse(throw RuntimeException("Wiring: Bad Init"))

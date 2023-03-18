package org.somecode.enig4s
package mach

import cats.implicits.*
import scala.collection.immutable.ArraySeq
import scala.collection.immutable.Queue

final case class MachineState(
  positions: ArraySeq[Glyph],
  rings: ArraySeq[Glyph],
  reflector: Glyph,
  plugboard: Option[EnigmaPlugBoard],
  symbols: SymbolMap,
  trace: Boolean
):
  def display(symbols: SymbolMap): String =
    val pos = displayPositions(symbols)
    val ring = symbols.glyphsToString(rings.reverse)
      .toOption
      .getOrElse("<invalid>")
    val reflect = symbols.glyphToPoint(reflector)
      .map(Character.toString)
      .toOption
      .getOrElse("<invalid>")
    val plug = plugboard.map(pb => s"${pb.mapping.size} mappings")
      .getOrElse("<not present>")

    s"pos[$pos] ring[$ring] ref[$reflect] plugs[$plug]"

  def displayPositions(symbols: SymbolMap): String =
    symbols.glyphsToString(positions.reverse)
      .toOption
      .getOrElse("<invalid>")

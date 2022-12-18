package org.somecode.enig4s
package mach

import cats.implicits.*
import scala.collection.immutable.ArraySeq
import scala.collection.immutable.Queue

final case class MachineState(
  positions: ArraySeq[Glyph],
  rings: ArraySeq[Glyph],
  reflector: Glyph,
  symbols: SymbolMap,
  traceQ: Option[Queue[String]] = Some(Queue.empty)
):
  def display(symbols: SymbolMap): String =
    val pos = symbols.glyphsToString(positions)
      .toOption
      .getOrElse("<invalid>")

    val ring = symbols.glyphsToString(rings)
      .toOption
      .getOrElse("<invalid>")

    val reflect = symbols.glyphToPoint(reflector)
      .map(Character.toString)
      .toOption
      .getOrElse("<invalid>")

    s"pos: $pos ring: $ring ref: $reflect"

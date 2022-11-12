package org.somecode.enig4s
package mach

import cats.implicits.*
import scala.collection.immutable.ArraySeq
import scala.collection.immutable.Queue

final case class MachineState(
  wheelState: ArraySeq[Glyph],
  reflectorState: Glyph,
  traceQ: Queue[String]
):
  def display(symbols: SymbolMap): String =
    val pos = symbols.glyphsToString(wheelState)
      .toOption
      .getOrElse("<invalid>")

    val reflect = symbols.glyphToPoint(reflectorState)
      .map(Character.toString)
      .toOption
      .getOrElse("<invalid>")

    s"pos: $pos ref: $reflect"

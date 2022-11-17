package org.somecode.enig4s
package mach

import cats.implicits.*
import scala.collection.immutable.ArraySeq
import scala.collection.immutable.Queue

final case class MachineState(
  wheelState: ArraySeq[Glyph],
  reflectorState: Glyph,
  traceQ: Option[Queue[String]] = None
):
  def trace(str: String) = traceQ
    .map(q => copy(wheelState, reflectorState, Some(q.enqueue(str))))
    .getOrElse(this)

  def display(symbols: SymbolMap): String =
    val pos = symbols.glyphsToString(wheelState)
      .toOption
      .getOrElse("<invalid>")

    val reflect = symbols.glyphToPoint(reflectorState)
      .map(Character.toString)
      .toOption
      .getOrElse("<invalid>")

    s"pos: $pos ref: $reflect"

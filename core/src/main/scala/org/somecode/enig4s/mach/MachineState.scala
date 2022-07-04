package org.somecode.enig4s
package mach

import cats.implicits.*
import scala.collection.immutable.ArraySeq

final case class MachineState(
  wheelState: IndexedSeq[WheelState],
  reflectorState: Position
):
  def readable(symbols: SymbolMap): String =
    val ws = wheelState
      .map(ws => symbols.codeToPoint(ws.position))
      .to(ArraySeq)
      .sequence
      .map(v => String(v.toArray.reverse, 0, v.size))
      .toOption
      .getOrElse("<unavailable>")

    val ref = symbols
      .codeToPoint(KeyCode.unsafe(reflectorState))
      .map(Character.toString)
      .toOption
      .getOrElse("<unavailable>")

    s"[state: $ws $ref]"

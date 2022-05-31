package org.somecode.enig4s
package mach

import cats.implicits.*
import scala.collection.immutable.ArraySeq

final case class MachineState(
  wheelState: IndexedSeq[WheelState],
  reflectorState: Position
):
  def wheelPositions(symbols: SymbolMap): String =
    wheelState
      .map(ws => symbols.codeToPoint(ws.position))
      .to(ArraySeq)
      .sequence
      .map(v => String(v.toArray, 0, v.size))
      .toOption
      .getOrElse("<unavailable>")

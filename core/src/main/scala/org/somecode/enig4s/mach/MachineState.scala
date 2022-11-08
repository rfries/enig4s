package org.somecode.enig4s
package mach

import cats.implicits.*
import scala.collection.immutable.ArraySeq

final case class MachineState(
  wheelState: ArraySeq[WheelState],
  reflectorState: Glyph
):
  def display(symbols: SymbolMap): String =
    val pos = wheelState
      .traverse(ws => symbols.codeToPoint(ws.position))
      .map(v => String(v.toArray.reverse, 0, v.size))
      .toOption
      .getOrElse("<invalid>")

    val ring = wheelState
      .traverse(ws => symbols.codeToPoint(KeyCode.unsafe(ws.ring)))
      .map(v => String(v.toArray.reverse, 0, v.size))
      .toOption
      .getOrElse("<invalid>")

    val reflect = symbols.codeToPoint(KeyCode.unsafe(reflectorState))
      .map(Character.toString)
      .toOption
      .getOrElse("<invalid>")

    s"pos: $pos ring: $ring ref: $reflect"

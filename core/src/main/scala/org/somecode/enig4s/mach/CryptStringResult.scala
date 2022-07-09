package org.somecode.enig4s
package mach

import scala.collection.immutable.Queue

final case class CryptStringResult(
  state: MachineState,
  text: String,
  trace: Option[IndexedSeq[String]]
):
  def traceMsg: String =
    s"""==> "$text"\n """ + trace.map(_.mkString("\n")).getOrElse("")

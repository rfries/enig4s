package org.somecode.enig4s
package mach

import cats.implicits.*
import scala.collection.immutable.Queue

final case class CryptStringResult(
  state: MachineState,
  text: String,
  trace: Option[IndexedSeq[String]]
):
  def traceMsg: String =
    state.traceQ.toVector.mkString("\n") + s"""==> "$text"\n """


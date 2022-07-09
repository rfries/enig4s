package org.somecode.enig4s
package mach

import scala.collection.immutable.Queue

final case class CryptResult(
  state: MachineState,
  result: KeyCode,
  trace: Option[Queue[String]]
)

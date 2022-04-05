package org.somecode.enig4s
package mach

final case class MachineState(
  wheelState: IndexedSeq[WheelState],
  reflectorState: WheelState
)

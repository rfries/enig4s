package org.somecode.enig4s
package jsapi

import org.somecode.enig4s.mach.Wheel

final case class MachineRequest(
  machineType: Option[String],
  wheels: Vector[WheelJs],
  ringSettings: String,
  wheelPositions: String,
  reflectorName: String,
  reflectorPosition: Option[String],
  plugs: Vector[String],
  text: String
)

package org.somecode.enig4s.server.api

import org.somecode.enigma.mach.Wheel

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

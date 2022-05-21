package org.somecode.enig4s.jsapi

import org.somecode.enig4s.mach.Machine
import org.somecode.enig4s.mach.MachineState

final case class MachineRequest(machine: Machine, state: MachineState, text: String)

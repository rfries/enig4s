package org.somecode.enigma
package mach

class Machine(
  rotors: Vector[Rotor],
  reflector: Rotor,
  wheelSettings: String,
  ringSettings: Vector[Position]):
    val wheels = rotors.size

end Machine

object Machine

end Machine

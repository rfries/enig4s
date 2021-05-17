package org.somecode.enigma
package mach

class Machine(
  wheels: Vector[Wheel],
  reflector: Wheel,
  wheelSettings: String,
  ringSettings: Vector[Position]):

end Machine

object Machine:

  final case class State(position: Vector[Position], val ringSettings: Vector[Position])

  trait Bus:
    def lookup(state: State, key: Position): Position
    def reverseLookup(state: State, key: Position): Position


package org.somecode.enigma
package mach

case class Machine(
  wheels: Vector[Wheel],
  reflector: Wheel,
  initialState: Machine.State)

object Machine:

  final case class State(
    position: Vector[Position],
    ringSettings: Vector[Position])

  trait Bus:
    def lookup(state: State, key: Position): Position
    def reverseLookup(state: State, key: Position): Position


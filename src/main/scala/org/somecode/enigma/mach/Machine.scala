package org.somecode.enigma
package mach

import cats.data.State

case class Machine(
  wheels: Vector[Wheel],
  ringSettings: Vector[KeyCode],
  reflector: Wheel,
  advance: Vector[State[Machine.WheelState, KeyCode]],
  initialSettings: Machine.MachineState)

object Machine:

  final case class MachineState(wheelState: Vector[WheelState])
  final case class WheelState(position: KeyCode, atNotch: Boolean, rightNotch: Boolean)

  trait Bus:
    def size: Int
    def translate(state: WheelState, key: KeyCode): KeyCode

  trait Rotor extends Bus:
    def cotranslate(state: WheelState, key: KeyCode): KeyCode

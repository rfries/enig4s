package org.somecode.enigma
package mach

import cats.data.State

case class Machine private (
  wheels: Vector[Wheel],
  ringSettings: Vector[KeyCode],
  reflector: Wheel,
  initialSettings: Machine.MachineState)

object Machine:

  def apply (
    wheels: Vector[Wheel],
    ringSettings: Vector[KeyCode],
    reflector: Wheel,
    initialSettings: MachineState): Either[String, Machine] = ???

  final case class MachineState(wheelState: Vector[WheelState])
  final case class WheelState(position: KeyCode)

  trait Bus:
    def size: Int
    def translate(state: WheelState, key: KeyCode): KeyCode

  trait Rotor extends Bus:
    def cotranslate(state: WheelState, key: KeyCode): KeyCode
    def advance(pos: KeyCode): KeyCode

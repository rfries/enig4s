package org.somecode.enigma
package mach

import cats.data.State
import Machine.Rotor

case class Machine private (
  wheels: Vector[ConfiguredWheel],
  reflector: Rotor,
  init: Machine.MachineState):

  def size: Int = reflector.size

object Machine:

  def apply (wheels: Vector[ConfiguredWheel],
             reflector: Rotor,
             init: MachineState): Either[String, Machine] =
    if wheels.exists(_.size != reflector.size) then
      Left("Wheel size must match reflector size.")
    else if wheels.size != init.wheelState.size then
      Left(s"Initial state vector size (${init.wheelState.size}) must match the wheel vector size (${wheels.size}).")
    else if init.wheelState.exists(_.position >= reflector.size) then
      Left(s"Initial state vector must contain only values less than ${reflector.size}.")
    else
      Right(new Machine(wheels, reflector, init))

  final case class MachineState(wheelState: Vector[WheelState])
  final case class WheelState(position: KeyCode)

  trait Bus:
    def size: Int
    def translate(state: WheelState, key: KeyCode): KeyCode

  trait Rotor extends Bus:
    def cotranslate(state: WheelState, key: KeyCode): KeyCode
    def advance(pos: KeyCode): KeyCode

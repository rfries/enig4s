package org.somecode.enigma
package mach

import cats.data.State
import Machine.{MachineState, Rotor, WheelState}

case class Machine private (
  wheels: Vector[ConfiguredWheel],
  reflector: Rotor):

  def size: Int = reflector.size

  def advance(start: MachineState): MachineState =

    def advanceIf(cond: Boolean, idx: Int): KeyCode =
      if (cond)
        start.wheelState(idx).position.plusMod(KeyCode.one)
      else
        start.wheelState(idx).position

    val atNotch = start.wheelState
      .map(_.position)
      .zip(wheels)
      .map { (pos, wheel) =>
        wheel.wheel.notches.contains(pos)
      }

    MachineState(
      Vector(
        WheelState(advanceIf(true, 0)),
        WheelState(advanceIf(atNotch(0) || atNotch(1), 1)),
        WheelState(advanceIf(atNotch(1), 2))
      )
    )

  def translate(state: MachineState, in: ValidKeys): Either[String, ValidKeys] = ???

object Machine:

  def apply (wheels: Vector[ConfiguredWheel],
             reflector: Rotor): Either[String, Machine] =
    if wheels.exists(_.size != reflector.size) then
      Left("Wheel sizes must match reflector size.")
    else
      Right(new Machine(wheels, reflector))

//    else if wheels.size != init.wheelState.size then
//      Left(s"Initial state vector size (${init.wheelState.size}) must match the wheel vector size (${wheels.size}).")
//    else if init.wheelState.exists(_.position >= reflector.size) then
//      Left(s"Initial state vector must contain only values less than ${reflector.size}.")

  final case class MachineState(wheelState: Vector[WheelState])
  final case class WheelState(position: KeyCode)

  trait Bus:
    def size: Int
    def translate(state: WheelState, key: KeyCode): KeyCode

  trait Rotor extends Bus:
    def cotranslate(state: WheelState, key: KeyCode): KeyCode

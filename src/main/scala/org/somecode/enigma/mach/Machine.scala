package org.somecode.enigma
package mach

import cats.data.State
import Machine.{MachineState, Rotor, WheelState}

case class Machine private (
  wheels: Vector[ConfiguredWheel],
  reflector: Rotor,
  kb: Wiring):

  def size: Int = reflector.size

  def advance(start: MachineState): MachineState =

    def advanceIf(idx: Int, cond: Boolean) =
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
        WheelState(advanceIf(0, true)),
        WheelState(advanceIf(1, atNotch(0) || atNotch(1))),
        WheelState(advanceIf(2, atNotch(1)))
      )
    )

  def translate(state: MachineState, in: ValidKeys): Either[String, (MachineState, ValidKeys)] = ???

object Machine:

  def apply (
    wheels: Vector[ConfiguredWheel],
    reflector: Rotor,
    kb: Wiring
  ): Either[String, Machine] =
    if wheels.exists(_.size != reflector.size) then
      Left("Wheel sizes must match reflector size.")
    else if kb.size != reflector.size then
      Left(s"Keyboard wiring size (${kb.size}) must match the reflector size (${reflector.size}).")
    else
      Right(new Machine(wheels, reflector, kb))

  final case class MachineState(wheelState: Vector[WheelState])
  final case class WheelState(position: KeyCode)

  trait Bus:
    def size: Int
    def translate(key: KeyCode): KeyCode
    def cotranslate(key: KeyCode): KeyCode

  trait Rotor:
    def size: Int
    def translate(state: WheelState, key: KeyCode): KeyCode
    def cotranslate(state: WheelState, key: KeyCode): KeyCode

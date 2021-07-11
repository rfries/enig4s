package org.somecode.enigma
package mach

import cats.data.State

case class Machine(
  wheels: Vector[Wheel],
  ringSettings: Vector[KeyCode],
  reflector: Wheel,
  advance: Vector[State[Machine.WState, KeyCode]],
  initialSettings: Machine.MState)

object Machine:

  final case class MState(wheelPositions: Vector[KeyCode])
  final case class WState(position: KeyCode)

  trait Bus:
    def size: Int
    def translate(state: WState, key: KeyCode): KeyCode
    def cotranslate(state: WState, key: KeyCode): KeyCode

package org.somecode.enigma
package mach

import cats.data.State
import Machine.{MachineState, Rotor, WheelState}
import scala.annotation.tailrec

case class Machine private (
  wheels: Vector[ConfiguredWheel],
  reflector: Reflector,
  kb: Wiring):

  def size: Int = reflector.size

  def advance(start: MachineState): MachineState =

    def advanceIf(idx: Int, cond: Boolean) =
      if (cond)
        start.wheelState(idx).position.plusMod(size, KeyCode.one)
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
      ),
      start.reflectorState
    )

  private def translateKeyCode(state: MachineState, in: KeyCode): KeyCode =
    // recursive, but not tailrec since reverse translation must happen after recursive call
    def translateRotor(wheelNum: Int, k: KeyCode): KeyCode =
      if wheelNum >= wheels.size then
        reflector.translate(state.reflectorState, k)
      else
        val wheel = wheels(wheelNum)
        val wheelState = state.wheelState(wheelNum)
        wheel.cotranslate(
          wheelNum,
          wheelState,
          translateRotor(wheelNum + 1, wheel.translate(wheelNum, wheelState, k))
        )
    val out = kb.cotranslate(translateRotor(0, kb.translate(in)))
    println(f"[$in%02d (${(in + 'A').toChar}) -> $out%02d (${(out + 'A').toChar})]")
    out

  def crypt(state: MachineState, in: KeyCode): Either[String, (MachineState, KeyCode)] =
    if (state.wheelState.size != wheels.size)
      Left(s"Wheel count in state (${state.wheelState.size}) does not match configuration (${wheels.size}).")
    else if (in < 0 || in >= size)
      Left(s"KeyCode ($in) not in range of rotor size ($size).")
    else
      val newState = advance(state)
      Right(newState, translateKeyCode(newState, in))

  def crypt(state: MachineState, in: ValidKeys): Either[String, (MachineState, ValidKeys)] =
    if (state.wheelState.size != wheels.size)
      Left(s"Wheel count in state (${state.wheelState.size}) does not match machine configuration (${wheels.size}).")
    else if (in.numCodes != size)
      Left(s"ValidKeys code size (${in.numCodes}) does not match rotor size ($size).")
    else
      @tailrec
      def next(state: MachineState, in: Vector[KeyCode], out: Vector[(MachineState, KeyCode)]): Vector[(MachineState, KeyCode)] =
        in match
          case k +: remaining =>
            val newState = advance(state)
            val crypted = translateKeyCode(newState, k)
            next(newState, remaining, out :+ newState -> crypted)
          case _ => out
      val res = next(state, in.codes, Vector.empty)
      val newState = res.lastOption.map(_._1).getOrElse(state)
      ValidKeys(in.numCodes, res.map(_._2)).map(keys => newState -> keys)

object Machine:

  def apply (
    wheels: Vector[ConfiguredWheel],
    reflector: Reflector,
    kb: Wiring
  ): Either[String, Machine] =
    if wheels.exists(_.size != reflector.size) then
      Left("Wheel sizes must match reflector size.")
    else if kb.size != reflector.size then
      Left(s"Keyboard wiring size (${kb.size}) must match the reflector size (${reflector.size}).")
    else
      Right(new Machine(wheels, reflector, kb))

  final case class WheelState(position: KeyCode)
  final case class MachineState(wheelState: Vector[WheelState], reflectorState: WheelState)

  /**
    * A fixed, stateless translation (for example, the keyboard map)
    */
  trait Bus:
    def size: Int
    def translate(key: KeyCode): KeyCode
    def cotranslate(key: KeyCode): KeyCode

  /**
    * A stateful translation using wheels (modulo position).  Only the caller
    * of translate/cotranslate knows the aggregate wheel state (machine state)
    * and can make decisions about advancement, so the wheel state is passed in
    * as a parameter as opposed to having its own [State].
    */
  trait Rotor:
    def size: Int
    def translate(wheelNum: Int, state: WheelState, key: KeyCode): KeyCode
    def cotranslate(wheelNum: Int, state: WheelState, key: KeyCode): KeyCode

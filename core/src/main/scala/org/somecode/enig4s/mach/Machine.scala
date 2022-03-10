package org.somecode.enig4s
package mach

import cats.data.State
import scala.annotation.tailrec

final case class Machine private (
  symbols: SymbolMap,
  kb: Wiring,
  wheels: IndexedSeq[Wheel],
  reflector: Reflector,
  plugs: PlugBoard
):
  def size: Int = symbols.size

  def advance(start: MachineState): MachineState =

    def advanceIf(idx: Int, cond: Boolean) =
      if (cond)
        start.wheelState(idx).position.next(size)
      else
        start.wheelState(idx).position

    val atNotch = start.wheelState
      .map(_.position)
      .zip(wheels)
      .map { (pos, wheel) =>
        wheel.notches.contains(pos)
      }

    MachineState(
      wheels.indices
        .map {
          case n @ 0 => (n, true)
          case n @ 1 => (n, atNotch(0) || atNotch(1))
          case n @ 2 => (n, atNotch(1))
          case n => (n, false)
        }.map {
          (idx, cond) => WheelState(Some(idx), advanceIf(idx, cond), start.wheelState(idx).ringSetting)
        },
      start.reflectorState
    )

  private def translateKeyCode(state: MachineState, in: KeyCode): KeyCode =

    // Recursive, but not tailrec since the return path translation happens after the
    // recursive call (i.e. after hitting the reflector, which is the bottom of the call stack)

    def translateRotor(wheelNum: Int, k: KeyCode): KeyCode =
      if wheelNum >= wheels.size then
        reflector.translate(state.reflectorState, k)
      else
        val wheel = wheels(wheelNum)
        val wheelState = state.wheelState(wheelNum)
        wheel.reverseTranslate(
          wheelNum,
          wheelState,
          translateRotor(wheelNum + 1, wheel.translate(wheelNum, wheelState, k))
        )
    end translateRotor

    val out =
      plugs.reverseTranslate(
        kb.reverseTranslate(
          translateRotor(0, kb.translate(plugs.translate(in)))
        )
      )
    println(f"m: $in%02d (${(in + 'A').toChar}) => $out%02d (${(out + 'A').toChar}) State: ${state.wheelState.map(_.position) :+ state.reflectorState.position}")
    out

  end translateKeyCode

  def crypt(state: MachineState, in: KeyCode): Either[String, (MachineState, KeyCode)] =
    if (state.wheelState.size != wheels.size)
      Left(s"Wheel count in state (${state.wheelState.size}) does not match configuration (${wheels.size}).")
    else if (in < 0 || in >= size)
      Left(s"KeyCode ($in) not in range of rotor size ($size).")
    else
      val newState = advance(state)
      Right(newState, translateKeyCode(newState, in))

  // def crypt(state: MachineState, in: ValidKeys): Either[String, (MachineState, ValidKeys)] =
  //   if (state.wheelState.size != wheels.size)
  //     Left(s"Wheel count in state (${state.wheelState.size}) does not match machine configuration (${wheels.size}).")
  //   else
  //     @tailrec
  //     def next(state: MachineState, in: Vector[KeyCode], out: Vector[(MachineState, KeyCode)]): Vector[(MachineState, KeyCode)] =
  //       in match
  //         case k +: remaining =>
  //           val newState = advance(state)
  //           val crypted = translateKeyCode(newState, k)
  //           next(newState, remaining, out :+ newState -> crypted)
  //         case _ => out
  //     val res = next(state, in.codes, Vector.empty)
  //     val newState = res.lastOption.map(_._1).getOrElse(state)
  //     ValidKeys(res.map(_._2)).map(keys => newState -> keys)
  // end crypt

  def crypt(state: MachineState, in: String): Either[String, (MachineState, String)] =
    if state.wheelState.size != wheels.size then
      Left(s"Wheel count in state (${state.wheelState.size}) does not match machine configuration (${wheels.size}).")
    // else if in.numCodes != size then
    //   Left(s"ValidKeys code size (${in.numCodes}) does not match rotor size ($size).")
    else

      @tailrec
      def next(state: MachineState, in: IndexedSeq[KeyCode], out: IndexedSeq[(MachineState, KeyCode)]): IndexedSeq[(MachineState, KeyCode)] =
        in match
          case k +: remaining =>
            val newState = advance(state)
            val crypted = translateKeyCode(newState, k)
            next(newState, remaining, out :+ newState -> crypted)
          case _ => out
      end next

      stringToValidKeys(in).flatMap {validKeys =>
        val res = next(state, validKeys.codes, Vector.empty)
        val newState = res.lastOption.map(_._1).getOrElse(state)
        symbols.codesToString(res.map(_._2)).map(out => (newState, out))
      }

  end crypt

  def stringToValidKeys(in: String): Either[String, ValidKeys] =
    symbols.stringToCodes(in).flatMap(ValidKeys.apply)

  sealed abstract case class ValidKeys private (codes: IndexedSeq[KeyCode]):
    override def toString: String = symbols.codesToString(codes).getOrElse("<invalid>")

  object ValidKeys:

    def apply(codes: IndexedSeq[KeyCode]): Either[String, ValidKeys] =
      if (codes.exists(k => k >= size))
        Left(s"All KeyCodes must be between 0 and ${size-1}.")
      else
        Right(new ValidKeys(codes) {})

end Machine

object Machine:

  def apply (
    symbolMap: SymbolMap,
    kb: Wiring,
    wheels: IndexedSeq[Wheel],
    reflector: Reflector,
    plugboard: PlugBoard
  ): Either[String, Machine] =
    if wheels.exists(_.size != symbolMap.size) then
      Left(s"Wheel sizes do not match the character map size (${symbolMap.size}).")
    else if kb.size != symbolMap.size then
      Left(s"Keyboard wiring size (${kb.size}) must match the character map size (${symbolMap.size}).")
    else if reflector.size != symbolMap.size then
      Left(s"Reflector size (${reflector.size}) must match the character map size (${symbolMap.size}).")
    else
      Right(new Machine(symbolMap, kb, wheels, reflector, plugboard))

end Machine

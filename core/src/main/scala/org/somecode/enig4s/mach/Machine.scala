package org.somecode.enig4s
package mach

import cats.implicits.*
import scala.annotation.tailrec

sealed abstract case class Machine(
  busSize: BusSize,
  symbols: SymbolMap,
  kb: Wiring,
  wheels: IndexedSeq[Wheel],
  reflector: Reflector,
  plugBoard: PlugBoard
):

  import Machine.*

  def advance(start: MachineState): MachineState =

    def advanceIf(idx: Int, cond: Boolean) =
      if (cond)
        start.wheelState(idx).position.next(busSize)
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
          (idx, cond) => WheelState(Some(idx), advanceIf(idx, cond), start.wheelState(idx).ring)
        },
      start.reflectorState
    )

  def crypt(state: MachineState, in: KeyCode): Either[String, (MachineState, KeyCode)] =
    if (state.wheelState.size != wheels.size)
      Left(s"Wheel count in state (${state.wheelState.size}) does not match configuration (${wheels.size}).")
    else if (in >= busSize)
      Left(s"KeyCode ($in) not in range of wheel size ($busSize).")
    else
      val newState: MachineState = advance(state)
      Right(newState, translate(newState)(in))

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
            val newState: MachineState = advance(state)
            //val crypted: KeyCode = translateKeyCode(newState, k)
            val crypted: KeyCode = translate(newState)(k)
            next(newState, remaining, out :+ ArrowAssoc(newState) -> crypted)
          case _ => out
      end next

      stringToValidKeys(in).flatMap {validKeys =>
        val res: scala.IndexedSeq[(MachineState, KeyCode)] = next(state, validKeys.codes, Vector.empty)
        val newState: MachineState = res.lastOption.map(_._1).getOrElse(state)
        symbols.codesToString(res.map(_._2)).map(out => (newState, out))
      }

  end crypt

  def stringToValidKeys(in: String): Either[String, ValidKeys] =
    symbols.stringToCodes(in).flatMap(ValidKeys.apply)

  private def translate(state: MachineState): KeyCode => KeyCode = in =>
      val wheelStates: IndexedSeq[(Wheel, WheelState)] = wheels.zip(state.wheelState)
      val funs = Vector(plugBoard.forward, kb.forward)
        ++ wheelStates.map { (wheel, state) => wheel.forward(state) }
        ++ Vector(reflector.forward(state.reflectorState))
        ++ wheelStates.reverse.map { (wheel, state) => wheel.reverse(state) }
        ++ Vector(kb.reverse, plugBoard.reverse)
      funs.reduceLeft((fall, f) => f.compose(fall))(in)

  sealed abstract case class ValidKeys private (codes: IndexedSeq[KeyCode]):
    override def toString: String = symbols.codesToString(codes).getOrElse("<invalid>")

  object ValidKeys:

    def apply(codes: IndexedSeq[KeyCode]): Either[String, ValidKeys] =
      if (codes.exists(k => k >= busSize))
        Left(s"All KeyCodes must be between 0 and ${busSize-1}, inclusive")
      else
        Right(new ValidKeys(codes) {})


end Machine

object Machine:

  def apply (
    size: Int,
    symbolMap: SymbolMap,
    keyboard: Wiring,
    wheels: IndexedSeq[Wheel],
    reflector: Reflector,
    plugBoard: PlugBoard
  ): Either[String, Machine] =
    for
      busSize <- BusSize(size)
      sm <- Either.cond(
        symbolMap.size === busSize,
        symbolMap,
        s"Symbol map size (${symbolMap.size}) does not match the bus size ($busSize)"
      )
      kb <- Either.cond(
        keyboard.size === busSize,
        keyboard,
        s"Keyboard wiring size (${keyboard.size}) must match the bus size ($busSize)"
      )
      wh <- Either.cond(
        wheels.forall(_.busSize === busSize),
        wheels,
        s"Wheel sizes ${wheels.map(_.busSize).mkString("(",",",")")} do not match the bus size ($busSize)"
      )
      ref <- Either.cond(
        reflector.busSize === busSize,
        reflector,
        s"Reflector size (${reflector.busSize}) does not match bus size ($busSize)"
      )
      plug <- Either.cond(
        plugBoard.size === busSize,
        plugBoard,
        s"Plugboard size (${plugBoard.size}) does not match bus size ($busSize)"
      )
    yield
      new Machine(busSize, sm, kb, wh, ref, plugBoard) {}

end Machine

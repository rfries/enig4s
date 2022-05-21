package org.somecode.enig4s
package mach

import fs2.{Pure, Stream}
import cats.implicits.*
import scala.annotation.tailrec
import scala.collection.immutable.ArraySeq

sealed abstract case class Machine(
  symbols: SymbolMap,
  entry: Wiring,
  wheels: IndexedSeq[Wheel],
  reflector: Reflector,
  plugBoard: Option[PlugBoard]
):

  val busSize: Int = entry.size

  def crypt(state: MachineState, in: Int): Either[String, (MachineState, KeyCode)] =
    if (state.wheelState.size != wheels.size)
      Left(s"Wheel count in state (${state.wheelState.size}) does not match configuration (${wheels.size}).")
    else if (in >= busSize)
      Left(s"KeyCode ($in) not in range of wheel size ($busSize).")
    else
      KeyCode(in).map( k =>
        val newState = advance(state)
        newState -> translate(newState)(k)
      )

  def crypt(state: MachineState, in: String): Either[String, (MachineState, String)] =
    if state.wheelState.size != wheels.size then
      Left(s"Wheel count in state (${state.wheelState.size}) does not match machine configuration (${wheels.size}).")
    else
      for
        validKeys <- stringToValidKeys(in)
        res = codeStream(validKeys, state).toVector
        endState = res.lastOption.map(_._1).getOrElse(state)
        out <- symbols.codesToString(res.map(_._2))
      yield (endState, out)

  private def stringToValidKeys(in: String): Either[String, ValidKeys] =
    symbols.stringToCodes(in).flatMap(ValidKeys.apply)

  private def codeStream(validKeys: ValidKeys, state: MachineState): Stream[Pure, (MachineState, KeyCode)] =
    Stream.emits(validKeys.codes)
      .mapAccumulate(state) ( (state, ch) =>
        val newState: MachineState = advance(state)
        (newState, translate(newState)(ch))
      )

  private def advance(start: MachineState): MachineState =
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

    start.copy(wheelState =
      wheels.indices
        .map {
          case n @ 0 => (n, true)
          case n @ 1 => (n, atNotch(0) || atNotch(1))
          case n @ 2 => (n, atNotch(1))
          case n => (n, false)
        }.map {
          (idx, cond) => WheelState(advanceIf(idx, cond), start.wheelState(idx).ring)
        }
    )

  def translate(state: MachineState): KeyCode => KeyCode = in =>
    val wheelStates: IndexedSeq[(Wheel, WheelState)] = wheels.zip(state.wheelState)

    val wheelFuns = Vector(entry.forward)
      ++ (wheelStates.map ((wheel, state) => wheel.forward(state)))
      .appended(reflector.forward(state.reflectorState))
      .concat(wheelStates.reverse.map((wheel, state) => wheel.reverse(state)))
      .appended(entry.reverse)

    val all = plugBoard
      .map(pb => pb.forward +: wheelFuns :+ pb.reverse)
      .getOrElse(wheelFuns)

    all.reduceLeft((fall, f) => f.compose(fall))(in)

  /** Represents a sequence of KeyCodes that has been validated for this instance
   *  of a Machine (path dependent type) */

  sealed abstract case class ValidKeys private (codes: IndexedSeq[KeyCode]):
    override def toString: String = symbols.codesToString(codes).getOrElse("<invalid>")

  object ValidKeys:

    def apply(codes: IndexedSeq[KeyCode]): Either[String, ValidKeys] =
      if (codes.exists(k => k >= busSize))
        Left(s"All KeyCodes must be between 0 and ${busSize-1}, inclusive")
      else
        Right(new ValidKeys(codes) {})

  /** Represents a machine state that has been validated for this instance
   *  of a Machine (path dependent type) */

  sealed abstract case class ValidState private (state: MachineState)

  object ValidState:
    def apply(state: MachineState): Either[String, ValidState] =
      for
        _ <-  state.wheelState
                .find(_.position >= entry.size)
                .map(ws => s"Wheel position (${ws.position}) is too large for bus ($busSize)")
                .toLeft(())
        _ <-  state.wheelState
                .find(_.ring >= busSize)
                .map(ws => s"Ring setting (${ws.ring}) is too large for bus ($busSize)")
                .toLeft(())
        _ <-  Either.cond(
                state.reflectorState < busSize,
                (),
                s"Reflector position (${state.reflectorState}) is too large for bus ($busSize)"
              )
        _ <-  Either.cond(
                reflector.positions.getOrElse(Vector(0)).contains(state.reflectorState),
                (),
                s"Reflector position (${state.reflectorState}) is not allowed for this reflector."
              )
        _ <-  Either.cond(
                state.wheelState.size != wheels.size,
                (),
                s"Wheel state size (${state.wheelState.size}) does not match number of wheels (${wheels.size})"
              )
      yield new ValidState(state) {}

object Machine:

  def apply (
    symbolMap: SymbolMap,
    keyboard: Wiring,
    wheels: IndexedSeq[Wheel],
    reflector: Reflector,
    plugBoard: Option[PlugBoard],
  ): Either[String, Machine] =
    for
      sm <- Either.cond(
        symbolMap.size === keyboard.size,
        symbolMap,
        s"Symbol map size (${symbolMap.size}) does not match the bus size (${keyboard.size})"
      )
      wh <- Either.cond(
        wheels.forall(_.size === keyboard.size),
        wheels,
        s"Wheel sizes ${wheels.map(_.size).mkString("(",",",")")} do not match the bus size (${keyboard.size})"
      )
      ref <- Either.cond(
        reflector.wiring.size === keyboard.size,
        reflector,
        s"Reflector size (${reflector.size}) does not match the bus size (${keyboard.size})"
      )
      plug <- plugBoard.map(pb =>
        Either.cond(
          pb.size === keyboard.size,
          pb,
          s"Plugboard size (${plugBoard.size}) does not match the bus size (${keyboard.size})"
        )
      ).getOrElse(Right(None))
    yield
      new Machine(sm, keyboard, wh, ref, plugBoard) {}

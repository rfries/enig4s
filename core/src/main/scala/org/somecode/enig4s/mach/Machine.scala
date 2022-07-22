package org.somecode.enig4s
package mach

import fs2.{Pure, Stream}
import cats.implicits.*
import scala.annotation.tailrec
import scala.collection.immutable.ArraySeq
import scala.collection.immutable.Queue

sealed abstract case class Machine(
  symbols: SymbolMap,
  entry: Wiring,
  wheels: IndexedSeq[Wheel],
  reflector: Reflector,
  plugBoard: Option[PlugBoard]
):

  import Machine.*

  val busSize: Int = entry.size

  def crypt(state: MachineState, in: Int, trace: Boolean): Either[String, CryptResult] =
    if (state.wheelState.size != wheels.size)
      Left(s"Wheel count in state (${state.wheelState.size}) does not match configuration (${wheels.size}).")
    else if (in >= busSize)
      Left(s"KeyCode ($in) not in range of wheel size ($busSize).")
    else
      KeyCode(in).map( k =>
        val newState = advance(state)
        val res = translate(newState, trace)(k)
        CryptResult(newState, res.result, res.trace)
      )

  def crypt(state: MachineState, in: String, trace: Boolean): Either[String, CryptStringResult] =
    if state.wheelState.size != wheels.size then
      Left(s"Wheel count in state (${state.wheelState.size}) does not match machine configuration (${wheels.size}).")
    else
      for
        validKeys <- stringToValidKeys(in)
        res = codeStream(validKeys, state, trace).toVector
        trc = res.map(_._2.trace).sequence.map(_.map(_.mkString("\n")))
        endState = res.lastOption.map(_._1).getOrElse(state)
        out <- symbols.codesToString(res.map(_._2.result))
      yield CryptStringResult(endState, out, trc)

  private def stringToValidKeys(in: String): Either[String, ValidKeys] =
    symbols.stringToCodes(in).flatMap(ValidKeys.apply)

  private def codeStream(validKeys: ValidKeys, state: MachineState, trace: Boolean): Stream[Pure, (MachineState, CryptResult)] =
    Stream.emits(validKeys.codes)
      .mapAccumulate(state) ( (state, ch) =>
        val newState: MachineState = advance(state)
        val res = translate(newState, trace)(ch)
        (newState, CryptResult(newState, res.result, res.trace))
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

  def translate(state: MachineState, trace: Boolean = false): KeyCode => MachineResult = in =>
    val wheelStates: IndexedSeq[((Wheel, WheelState), Int)] = wheels.zip(state.wheelState).zipWithIndex

    val wheelFuns = List(("[<- k]", entry.forward))
      ++ (wheelStates.map { case ((wheel, state), idx) => (s"[<- $idx]", wheel.forward(state)) } )
      .appended(("[<> r]", reflector.forward(state.reflectorState)))
      .concat(wheelStates.reverse.map { case ((wheel, state), idx) => (s"[-> $idx]", wheel.reverse(state)) } )
      .appended("[-> k]", entry.reverse)

    val all = plugBoard
      .map(pb => ("[<- p]", pb.forward) +: wheelFuns :+ ("[-> p]", pb.reverse))
      .getOrElse(wheelFuns)

    if trace then
      @tailrec
      def next(funs: List[(String, KeyCode => KeyCode)], traceQ: Queue[String], k: KeyCode): (KeyCode, Queue[String]) =
        funs match
          case (label, f) :: rest =>
            val out = f(k)
            next(rest, traceQ.enqueue(traceTranslate(label, k, out)), out)
          case Nil =>
            (k, traceQ)
      val (out, traceItems) = next(all, Queue.empty, in)
      val inChar: String = symbols.displayCode(in)
      val outChar: String = symbols.displayCode(out)
      MachineResult(out, Some(Queue(f"""[state: ${state.readable(symbols)}] $inChar ($in%02d) => $outChar ($out%02d)""") ++ traceItems))
    else
      MachineResult(all.map(_._2).reduceLeft((fall, f) => f.compose(fall))(in), None)

  def traceTranslate(label: String, in: KeyCode, out: KeyCode): String =
    val inChar: String = symbols.displayCode(in)
    val outChar: String = symbols.displayCode(out)
    f"  $label%s $inChar ($in%02d) -> $outChar ($out%02d)"

  /**
   * Represents a sequence of KeyCodes that has been validated for this instance
   * of a Machine (path dependent type)
   */

  sealed abstract case class ValidKeys private (codes: IndexedSeq[KeyCode]):
    override def toString: String = symbols.codesToString(codes).getOrElse("<invalid>")

  object ValidKeys:

    def apply(codes: IndexedSeq[KeyCode]): Either[String, ValidKeys] =
      if (codes.exists(k => k >= busSize))
        Left(s"All KeyCodes must be between 0 and ${busSize-1}, inclusive")
      else
        Right(new ValidKeys(codes) {})

  /** This is a path-dependent type that represents a machine state that has been
   *  validated for this instance of a Machine */

  sealed abstract case class ValidState private (state: MachineState)

  object ValidState:
    def apply(state: MachineState): Either[String, ValidState] =
      for
        _ <-  state.wheelState
                .find(_.position >= entry.size)
                .map(ws => s"Wheel position (${ws.position}) is too large for bus ($busSize)")
                .toLeft(())
        _ <-  state.wheelState
                .find(_.ring.toInt >= busSize)
                .map(ws => s"Ring setting (${ws.ring}) is too large for bus ($busSize)")
                .toLeft(())
        _ <-  Either.cond(
                state.reflectorState.toInt < busSize,
                (),
                s"Reflector position (${state.reflectorState}) is too large for bus ($busSize)"
              )
        _ <-  Either.cond(
                reflector.positions.getOrElse(Vector(0)).contains(state.reflectorState),
                (),
                s"Reflector position (${state.reflectorState}) is not allowed for this reflector."
              )
        _ <-  Either.cond(
                state.wheelState.size === wheels.size,
                (),
                s"Number of wheels in state (${state.wheelState.size}) does not match number of wheels in Machine (${wheels.size})"
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

  final case class MachineResult(result: KeyCode, trace: Option[Queue[String]])

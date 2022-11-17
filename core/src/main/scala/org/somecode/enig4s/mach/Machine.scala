package org.somecode.enig4s
package mach

import fs2.{Pure, Stream}
import cats.implicits.*
import scala.annotation.tailrec
import scala.collection.immutable.ArraySeq
import scala.collection.immutable.Queue

sealed abstract case class Machine(
  symbols: SymbolMap,
  entry: Entry,
  wheels: ArraySeq[Wheel],
  reflector: Reflector,
  plugBoard: Option[PlugBoard]
):
  import entry.modulus

  import Machine.*

  def crypt(state: MachineState, in: Int, trace: Boolean): Either[String, CryptResult] =
    if (state.wheelState.size != wheels.size)
      Left(s"Wheel count in state (${state.wheelState.size}) does not match configuration (${wheels.size}).")
    else if (in >= entry.length)
      Left(s"KeyCode ($in) not in range of wheel size (${entry.length}).")
    else
      Glyph(in).map( g =>
        val newState = advance(state)
        val (endState, out) = transformer(newState, g)
        CryptResult(endState, out)
      )

  def crypt(state: MachineState, in: String, trace: Boolean): Either[String, CryptStringResult] =
    if state.wheelState.size != wheels.size then
      Left(s"Wheel count in state (${state.wheelState.size}) does not match machine configuration (${wheels.size}).")
    else
      for
        validGlyphs <- validateGlyphs(in)
        res = codeStream(validGlyphs, state, trace).toVector
        trc = res.traverse(_._1.traceQ).map(_.map(_.mkString("\n")))
        endState = res.lastOption.map(_._1).getOrElse(state)
        out <- symbols.glyphsToString(res.map(_._2))
      yield CryptStringResult(endState, out, trc)

  private def validateGlyphs(in: String): Either[String, ValidGlyphs] =
    symbols.stringToGlyphs(in).flatMap(ValidGlyphs.apply)

  private def codeStream(glyphs: ValidGlyphs, state: MachineState, trace: Boolean): Stream[Pure, (MachineState, Glyph)] =
    Stream.emits(glyphs.glyphs)
      .mapAccumulate(state) ( (state, in) =>
        transformer(advance(state), in)
      )

  private def advance(start: MachineState): MachineState =
    def advanceIf(idx: Int, cond: Boolean) =
      if (cond)
        start.wheelState(idx).next
      else
        start.wheelState(idx)

    val atNotch = start.wheelState
      .zip(wheels)
      .map((pos, wheel) => wheel.notchedAt(pos))

    start.copy(wheelState =
      wheels.indices
        .map {
          case n @ 0 => (n, true)
          case n @ 1 => (n, atNotch(0) || atNotch(1))
          case n @ 2 => (n, atNotch(1))
          case n => (n, false)
        }.map {
          (idx, cond) => advanceIf(idx, cond)
        }
        .to(ArraySeq)
    )

  protected[mach] def transformer: Transformer = (state, glyph) =>

    val wheelFuns = Vector(entry.forward)
      ++ (wheels.map(_.forward) :+ reflector.transformer)
      ++ (wheels.reverse.map(_.reverse) :+ entry.reverse)

    val allFuns = plugBoard.map(pb => pb.forward +: wheelFuns :+ pb.reverse)
      .getOrElse(wheelFuns)

    Function.chain[(MachineState, Glyph)](allFuns)(state, glyph)

  /**
   * Represents a sequence of [[Glyph]]s that has been validated for this instance
   * of a Machine (path dependent type).
   */

  sealed abstract case class ValidGlyphs private (glyphs: IndexedSeq[Glyph]):
    override def toString: String = symbols.glyphsToString(glyphs).getOrElse("<invalid>")

  object ValidGlyphs:

    def apply(glyphs: IndexedSeq[Glyph]): Either[String, ValidGlyphs] =
      if glyphs.exists(g => g.invalidFor(entry.length)) then
        Left(s"All KeyCodes must be between 0 and ${entry.length-1}, inclusive")
      else
        Right(new ValidGlyphs(glyphs) {})

  /**
   * This is a path-dependent type that represents a machine state that has been
   * validated for this instance of a Machine
   */

  sealed abstract case class ValidState private (state: MachineState)

  object ValidState:
    def apply(state: MachineState): Either[String, ValidState] =
      for
        _ <-  Either.cond(
                state.wheelState.size === wheels.size,
                (),
                s"Number of wheels in state (${state.wheelState.size}) does not match number of wheels in Machine (${wheels.size})"
              )
        _ <-  state.wheelState
                .find(_.invalidFor(entry.length))
                .map(g => s"Wheel position ($g) is too large for bus (${entry.length})")
                .toLeft(())
        _ <-  Either.cond(
                state.reflectorState.validFor(entry.length),
                (),
                s"Reflector position (${state.reflectorState}) is too large for bus ($entry.length)"
              )
        _ <-  Either.cond(
                reflector.positions.map(_.contains(state.reflectorState)).getOrElse(true),
                (),
                s"Reflector position (${state.reflectorState}) is not allowed for this reflector."
              )
      yield new ValidState(state) {}

object Machine:

  def apply (
    symbolMap: SymbolMap,
    entry: Entry,
    wheels: IndexedSeq[Wheel],
    reflector: Reflector,
    plugBoard: Option[PlugBoard]
  ): Either[String, Machine] =
    for
      sm <- Either.cond(
        symbolMap.size === entry.length,
        symbolMap,
        s"Symbol map size (${symbolMap.size}) does not match the bus size (${entry.length})"
      )
      wh <- Either.cond(
        wheels.forall(_.length === entry.length),
        wheels,
        s"Wheel sizes ${wheels.map(_.length).mkString("(",",",")")} do not match the bus size (${entry.length})"
      )
      ref <- Either.cond(
        reflector.wiring.length === entry.length,
        reflector,
        s"Reflector size (${reflector.length}) does not match the bus size (${entry.length})"
      )
      plug <- plugBoard.map(pb =>
        Either.cond(
          pb.length === entry.length,
          pb,
          s"Plugboard size (${plugBoard.size}) does not match the bus size (${entry.length})"
        )
      ).getOrElse(Right(None))
    yield
      new Machine(sm, entry, wh.to(ArraySeq), ref, plugBoard) {}

  final case class MachineResult(result: KeyCode, trace: Option[Queue[String]])

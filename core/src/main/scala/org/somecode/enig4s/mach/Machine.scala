package org.somecode.enig4s
package mach

import cats.*
import cats.data.Chain
import cats.implicits.*
import cats.syntax.*
import fs2.{Pure, Stream}

import scala.annotation.tailrec
import scala.collection.immutable.ArraySeq

sealed abstract case class Machine(
  entry: Entry,
  wheels: ArraySeq[Wheel],
  reflector: Reflector,
  symbols: SymbolMap
):
  import entry.wiring.modulus

  /**
    * Compose an aggregate transformer for the forward and return paths
    * through the plugboard, entry disc, wheels, and reflector.
    */
  protected[mach] val transformer: Transformer = (state, glyph) =>
    // entry disc -> wheels -> reflector -> wheels.reverse -> entry disc
    val wheelfuns = Vector(entry.forward)
      :++ wheels.map(_.forward)
      :+  reflector.reflect
      :++ wheels.reverse.map(_.reverse)
      :+  entry.reverse

    // bracket the wheel transformers with the plugboard, if defined
    val allfuns = state.plugboard match
      case Some(pb) => pb.forward +: wheelfuns :+ pb.reverse
      case None     => wheelfuns

    allfuns.foldLeftM(glyph) { (g, transform) =>
      transform(state, g)
    }

  def crypt(state: MachineState, in: String): Either[String, CryptStringResult] =
    for
      validState <- ValidState(state)
      validGlyphs <- validateGlyphs(in)
      (endState, runs) = runAll(validGlyphs, validState)
      outGlyphs = runs.map(_._2)
      outText <- symbols.glyphsToString(outGlyphs)
    yield CryptStringResult(endState, outText, formatTrace(runs.map(_._1)))

  private def formatTrace(traces: ArraySeq[Chain[String]]): Option[String] =
    Some(traces.map(_.toList.mkString("\n")).mkString("\n\n"))

  private def validateGlyphs(in: String): Either[String, ValidGlyphs] =
    symbols.stringToGlyphs(in).flatMap(ValidGlyphs.apply)

  private def runAll(validGlyphs: ValidGlyphs, validState: ValidState): (MachineState, ArraySeq[(Chain[String],Glyph)]) =
    validGlyphs.glyphs.mapAccumulate(validState.state)((state, glyph) =>
      val newState = advance(state)
      (newState, transformer(newState, glyph).run)
    )

  /** Advance to the next position */
  private def advance(start: MachineState): MachineState =
    def advanceIf(idx: Int, cond: Boolean) =
      if (cond)
        start.positions(idx).next
      else
        start.positions(idx)

    val atNotch = start.positions
      .zip(wheels)
      .map((pos, wheel) => wheel.notchedAt(pos))

    // Enigma will step the 2nd wheel when either the wheel to the left
    // is at a notch or the wheel itself is at a notch ("double stepping")
    start.copy(positions =
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

  /**
   * Represents a sequence of [[Glyph]]s that has been validated for this instance
   * of a Machine (path dependent type).
   */

  sealed abstract case class ValidGlyphs private (glyphs: ArraySeq[Glyph]):
    override def toString: String = symbols.glyphsToString(glyphs).getOrElse("<invalid>")

  object ValidGlyphs:

    def apply(glyphs: ArraySeq[Glyph]): Either[String, ValidGlyphs] =
      if glyphs.exists(g => g.invalidFor(entry.length)) then
        Left(s"All glyphs must be between 0 and ${entry.length-1}, inclusive")
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
                state.positions.size === wheels.size,
                (),
                s"Size of position vector in state (${state.positions.size}) does not match number of wheels in Machine (${wheels.size})"
              )
        _ <-  Either.cond(
                state.rings.size === wheels.size,
                (),
                s"Size of Ring Settings vector in state (${state.rings.size}) does not match number of wheels in Machine (${wheels.size})"
              )
        _ <-  state.positions
                .find(_.invalidFor(entry.length))
                .map(g => s"Wheel position ($g) is too large for bus (${entry.length})")
                .toLeft(())
        _ <-  Either.cond(
                state.reflector.validFor(entry.length),
                (),
                s"Reflector position (${state.reflector}) is too large for bus ($entry.length)"
              )
        _ <-  state.plugboard match
                case Some(pb) => if pb.length === entry.length then Right(()) else
                  Left(s"Plugboard size (${pb.length}) does not match the bus size (${entry.length})")
                case None => Right(())
      yield new ValidState(state) {}

object Machine:

  def apply (
    entry: Entry,
    wheels: IndexedSeq[Wheel],
    reflector: Reflector,
    symbolMap: SymbolMap
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
    yield
      new Machine(entry, wh.to(ArraySeq), ref, sm) {}

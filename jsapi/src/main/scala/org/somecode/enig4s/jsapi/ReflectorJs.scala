package org.somecode.enig4s
package jsapi

import cats.implicits.*
import io.circe.Codec
import io.circe.generic.semiauto.*
import org.somecode.enig4s.mach.{Cabinet, SymbolMap, Wiring, Position, Reflector}
import scala.collection.immutable.ArraySeq

// name or wiring required,
// if name is provided, no other attributes allowed
// else with wiring, positions and advance have defaults
final case class ReflectorJs(
  name: Option[String],
  wiring: Option[CodesJs],
  positions: Option[PositionsJs],
  advance: Option[Boolean]
):
  def toReflector(symbols: SymbolMap, cabinet: Cabinet): Either[String, Reflector] =
    this match
      case ReflectorJs(Some(nm), None, None, None) =>
        cabinet.reflectors.get(nm).toRight(s"Reflector '$nm' not found")

      case ReflectorJs(None, Some(wiresJs), posOpt, advOpt) =>
        for
          wireCodes <- wiresJs.toCodes(symbols)
          wires <- Wiring.apply(wireCodes)
          pos <- posOpt.map(_.toPositions(symbols)).getOrElse(Right(ArraySeq.empty[Position]))
          ref <- Reflector(wires, pos, advOpt.getOrElse(false))
        yield ref

      case _ => Left("One of 'name' or 'wiring' must be provided for a reflector")

object ReflectorJs:
  given Codec[ReflectorJs] = deriveCodec
package org.somecode.enig4s.jsapi

import cats.implicits.*
import io.circe.Codec
import io.circe.generic.semiauto.*
import org.somecode.enig4s.mach.{Cabinet, SymbolMap, Wiring}

final case class WiringJs(name: Option[String], wiring: Option[CodesJs]):
  def toWiring(symbols: SymbolMap, cabinet: Cabinet): Either[String, Wiring] = this match
    case WiringJs(Some(nm), None) => cabinet.wirings.get(nm).toRight(s"Wiring name '$nm' not found'")
    case WiringJs(None, Some(wires)) => wires.toCodes(symbols).flatMap(Wiring.apply)
    case _ => Left("One of 'name' or 'symbols' (but not both) must be specified")

object WiringJs:
  given Codec[WiringJs] = deriveCodec

package org.somecode.enig4s
package jsapi

import cats.implicits.*
import io.circe.Codec
import io.circe.generic.semiauto.*
import org.somecode.enig4s.mach.{Cabinet, KeyCode, SymbolMap, Wheel}

final case class WheelJs(
  name: Option[String],
  wiring: Option[WiringJs],
  notches: Option[CodesJs]
):
  def toWheel(symbols: SymbolMap, cabinet: Cabinet): Either[String, Wheel] = this match
    case WheelJs(Some(name), None, None) => cabinet.wheels.get(name).toRight(s"Wheel '$name' not found'")
    case WheelJs(None, Some(wires), Some(notch)) =>
      for
        wr <- wires.toWiring(symbols, cabinet)
        nt <- notch.toCodes(symbols)
        wh <- Wheel(wr, nt.toSet)
      yield wh
    case _ => Left(s"Wheels must contain either 'name' or 'mapping' and 'notches' only.")

object WheelJs:
  given Codec[WheelJs] = deriveCodec[WheelJs]

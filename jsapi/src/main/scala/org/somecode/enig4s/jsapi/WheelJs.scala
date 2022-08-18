package org.somecode.enig4s
package jsapi

import cats.implicits.*
import io.circe.Codec
import io.circe.generic.semiauto.*
import org.somecode.enig4s.mach.{Cabinet, KeyCode, SymbolMap, Wheel, Wiring}

final case class WheelJs(
  name: Option[String],
  wiring: Option[CodesJs],
  notches: Option[CodesJs]
):
  def toWheel(symbols: SymbolMap, cabinet: Cabinet): Either[String, Wheel] = this match
    case WheelJs(Some(name), None, None) => cabinet.findWheel(name).toRight(s"Wheel '$name' not found'")
    case WheelJs(None, Some(wires), Some(notch)) =>
      for
        codes <- wires.toCodes(symbols)
        wr <- Wiring(codes)
        nt <- notch.toCodes(symbols)
        wh <- Wheel(wr, nt)
      yield wh
    case _ => Left(s"Wheels must contain either 'name' or 'mapping' and 'notches' only.")

object WheelJs:
  given Codec[WheelJs] = deriveCodec[WheelJs]

package org.somecode.enig4s
package jsapi

import cats.implicits.*
import io.circe.Codec
import io.circe.generic.semiauto.*
import org.somecode.enig4s.mach.KeyCode

case class CodesJs(symbols: Option[String], codes: Option[Vector[Int]]):
  def toCodes: Either[String, Vector[KeyCode]] = this match
    case CodesJs(Some(str), None) => Right(str.map(KeyCode.unsafe(_)).toVector)
    case CodesJs(None, Some(arr)) => arr.map(KeyCode.apply).sequence
    case _ => Left("One (and only one) of 'codes' or 'symbols' must be specified.")

object CodesJs:
  given Codec[CodesJs] = deriveCodec

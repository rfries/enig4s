package org.somecode.enig4s
package jsapi

import cats.implicits.*
import io.circe.Codec
import io.circe.generic.semiauto.*
import org.somecode.enig4s.mach.KeyCode
import org.somecode.enig4s.mach.SymbolMap
import org.somecode.enig4s.mach.Wiring

import scala.collection.immutable.ArraySeq

/**
  * This class usually represents a [[Wiring]], though it is also used
  * with other entities that need a lookup table that can be specified
  * by either an array (of integers) or a symbolic string (in the context
  * of a symbol map), such as [[SymbolMap]] and [[MachineState]].
  *
  * @param symbols  The symbolic representation of the map, must be `None` if `codes` is provided.
  * @param codes    The array representation of the map, must be `None` if `symbols` is provided.
  */
case class CodesJs(symbols: Option[String], codes: Option[IndexedSeq[Int]]):
  def toCodes(symbols: SymbolMap): Either[String, ArraySeq[KeyCode]] =
    this match
      case CodesJs(Some(str), None) => symbols.stringToCodes(str)
      case CodesJs(None, Some(arr)) => arr.to(ArraySeq).traverse(KeyCode.apply)
      case _ => Left("One (and only one) of 'codes' or 'symbols' must be specified.")

object CodesJs:
  given Codec[CodesJs] = deriveCodec

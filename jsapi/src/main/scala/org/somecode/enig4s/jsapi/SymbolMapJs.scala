package org.somecode.enig4s
package jsapi

import cats.implicits.*
import io.circe.Codec
import io.circe.generic.semiauto.*
import org.somecode.enig4s.mach.{Cabinet, SymbolMap}

final case class SymbolMapJs(name: Option[String], mapping: Option[GlyphsJs]):
  def toSymbolMap(cabinet: Cabinet): Either[String, SymbolMap] = this match
    case SymbolMapJs(Some(nm), None) =>
      cabinet.findSymbolMap(nm).toRight(s"Symbol map '$nm' not found")
    case SymbolMapJs(None, Some(map)) =>
      map.toInts(SymbolMap.AZ).flatMap(SymbolMap.apply)
    case _ => Left("Must specify one (and only one) of 'name' or 'mapping' in symbol map.")

object SymbolMapJs:
  given Codec[SymbolMapJs] = deriveCodec

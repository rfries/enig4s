package org.somecode.enig4s
package server
package api

final case class WheelJs(
  name: Option[String],
  mapping: Option[String],
  notches: Option[String],
  position: String,
  ringSetting: String
)

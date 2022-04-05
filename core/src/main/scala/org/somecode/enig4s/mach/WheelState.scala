package org.somecode.enig4s
package mach

final case class WheelState(
  /** wheelNum is only here until logging/tracing is sorted out */
  wheelNum: Option[Int],
  position: KeyCode,
  ring: RingSetting
):
  val offset: Int = position - ring

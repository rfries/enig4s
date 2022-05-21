package org.somecode.enig4s
package mach

final case class WheelState(
  position: KeyCode,
  ring: RingSetting
):
  val offset: Int = position - ring

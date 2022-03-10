package org.somecode.enig4s
package mach

final case class WheelState(wheelNum: Option[Int], position: KeyCode, ringSetting: RingSetting):
  val offset: Int = position - ringSetting

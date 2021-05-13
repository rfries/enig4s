package org.somecode.enigma
package mach

import Wiring.Paths

opaque type Value = Int

object Value:

  def apply(c: Int): Either[String, Value] =
    if (c < 0 || c >= Paths)
      Left(s"Position must be between 0 and ${Paths-1}, inclusive.)")
    else
      Right(c)

  def unsafe(n: Int): Value = apply(n) match
    case Left(s) => throw new IllegalArgumentException(s)
    case Right(v) => v

  extension (vv: Value)
    def next: Value = (vv + 1) % Paths
    def value: Int = vv
    def +(other: Value): Value = (vv + other) % Paths
    
    def -(other: Value): Value =
      val diff = vv - other
      /* if diff is less than zero, then we must use the
         compliment of the mod to match the wheel markings */
      if diff < 0 then Paths + diff % Paths else diff % Paths

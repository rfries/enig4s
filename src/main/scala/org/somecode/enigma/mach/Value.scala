package org.somecode.enigma
package mach

// final case class Value private (v: Int):
//   def copy(v: Int = v) = Value.apply(v)

opaque type Value = Int

object Value:
  val Max = Wiring.Lines

  def apply(c: Int): Either[String, Value] =
    if (c < 0 || c >= Max)
      Left(s"Position must be between 0 and ${Max-1}, inclusive.)")
    else
      Right(c)

  def unsafe(c: Int): Value = apply(c) match
    case Left(s) => throw new IllegalArgumentException(s)
    case Right(v) => v

  extension (vv: Value)
    def next: Int = ((vv + 1) % Value.Max)
    def value: Int = vv
    def +(other: Value): Value = (vv + other) % Max
    def -(other: Value): Value =
      val diff = vv - other
      if diff < 0 then Max - Math.abs(diff % Max) else diff % Max

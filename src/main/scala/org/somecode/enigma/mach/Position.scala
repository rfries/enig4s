package org.somecode.enigma
package mach

opaque type Position <: Int = Int
object Position:

  val Max = 26

  def apply(c: Int): Either[String, Position] =
    if (c < 0 || c >= Max)
      Left(s"Position must be between 0 and ${Max-1}, inclusive.)")
    else
      Right(c)

  def unsafe(n: Int): Position = apply(n).fold(
    s => throw new IllegalArgumentException(s),
    v => v); 

  extension (p: Position)
    def next: Position = (p + 1) % Max
    def +(other: Position): Position = (p + other) % Max
    def -(other: Position): Position =
      val diff = p - other
      /* if diff is less than zero, then we must use the
         compliment of the mod to match the wheel markings */
      if diff < 0 then Max + diff % Max else diff % Max
  
  val zero = Position.unsafe(0)

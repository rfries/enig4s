package org.somecode.enigma
package mach

opaque type KeyCode = Int
object KeyCode:

  val Max = 26

  def apply(c: Int): Either[String, KeyCode] =
    if (c < 0 || c >= Max)
      Left(s"Position must be between 0 and ${Max-1}, inclusive.)")
    else
      Right(c)

  def unsafe(n: Int): KeyCode = apply(n).fold(
    s => throw new IllegalArgumentException(s),
    v => v); 

  extension (p: KeyCode)
    def toInt: Int = p
    def next: KeyCode = KeyCode.unsafe((p.toInt + 1) % Max)
    def +(other: KeyCode): KeyCode = KeyCode.unsafe((p + other) % Max)
    def -(other: KeyCode): KeyCode =
      val diff = p - other
      /* if diff is less than zero, then we must use the
         compliment of the mod to match the wheel markings */
      KeyCode.unsafe(if diff < 0 then (Max + diff) % Max else diff % Max)
  
  //given Ordering[Position] = summon[Ordering[Int]]
  
  val zero = KeyCode.unsafe(0)

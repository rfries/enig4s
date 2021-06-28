package org.somecode.enigma
package mach

opaque type KeyCode = Int
object KeyCode:

  def apply(c: Int, pred: Int => Boolean): Either[String, KeyCode] =    
    Either.cond(pred(c), c, s"KeyCode ($c) must be >= 0.")

  def unsafe(n: Int): KeyCode = apply(n).fold(
    s => throw new IllegalArgumentException(s),
    v => v); 

  extension (p: KeyCode)
    def toInt: Int = p
    def next(mod: Int): KeyCode = KeyCode.unsafe((p.toInt + 1) % mod)
    def plusMod(other: KeyCode, mod: Int): KeyCode = KeyCode.unsafe((p + other) % mod)
    def minusMod(other: KeyCode, mod: Int): KeyCode =
      val diff = p - other
      /* if diff is less than zero, then we must use the
         compliment of the mod to match the wheel markings */
      KeyCode.unsafe(if diff < 0 then (mod + diff) % mod else diff % mod)
  
  //given Ordering[Position] = summon[Ordering[Int]]
  
  val zero = KeyCode.unsafe(0)

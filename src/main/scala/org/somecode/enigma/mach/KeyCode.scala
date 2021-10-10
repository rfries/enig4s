package org.somecode.enigma
package mach

opaque type KeyCode <: Int = Int
object KeyCode:

  val zero = unsafe(0)
  val one = unsafe(1)

  def apply(c: Int): Either[String, KeyCode] =
    Either.cond(c >= 0, c, s"KeyCode ($c) must be >= 0.")

  /** Chars are unsigned, and so are always positive (or zero) */
  def apply(c: Char): KeyCode = c.toInt

  def unsafe(n: Int): KeyCode = apply(n).fold(
    s => throw new IllegalArgumentException(s),
    v => v);

  extension (k: KeyCode)

    def next(mod: Int): KeyCode = KeyCode.unsafe((k + 1) % mod)

    def plusMod(mod: Int, others: KeyCode*): KeyCode =
      KeyCode.unsafe((k + others.fold(0)(_+_)) % mod)

    def minusMod(mod: Int, others: KeyCode*): KeyCode =
      val diff = k - others.fold(0)(_+_)
      /* if diff is less than zero, then we must use the
         compliment of the mod to match the wheel markings */
      KeyCode.unsafe(if diff < 0 then (mod + diff) % mod else diff % mod)

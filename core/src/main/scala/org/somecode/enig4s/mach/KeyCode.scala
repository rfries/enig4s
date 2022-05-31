package org.somecode.enig4s
package mach

import cats.Eq
import scala.util.FromDigits
import scala.util.FromDigits.NumberTooSmall

opaque type KeyCode <: Int = Int
object KeyCode:

  val zero: KeyCode = unsafe(0)
  val one: KeyCode = unsafe(1)

  def apply(c: Int): Either[String, KeyCode] =
    Either.cond(c >= 0, c, s"KeyCode ($c) must be >= 0.")

  def apply(c: Char): KeyCode = c.toInt - 'A'

  def unsafe(n: Int): KeyCode = apply(n).fold(
    s => throw new IllegalArgumentException(s),
    v => v)

  given Eq[KeyCode] = summon[Eq[Int]]

  extension (k: KeyCode)

    def next(mod: Int): KeyCode = KeyCode.unsafe((k + 1) % mod)

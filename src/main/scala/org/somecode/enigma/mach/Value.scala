package org.somecode.enigma
package mach

final case class Value private (v: Int):
  def copy(v: Int = v) = Value.apply(v)

object Value:
  val Max = Wiring.Lines

  def apply(c: Int): Either[String, Value] =
    if (c < 0 || c >= Max)
      Left(s"Position must be between 0 and ${Max-1}, inclusive.)")
    else
      Right(new Value(c))

  def unsafe(c: Int): Value = apply(c) match
    case Left(s) => throw new IllegalArgumentException(s)
    case Right(v) => v


package org.somecode.enigma
package mach

opaque type KeyCode = Int
object KeyCode:

  def apply(c: Int): Either[String, KeyCode] =    
    Either.cond(c >= 0, c, s"KeyCode ($c) must be >= 0.")

  def unsafe(n: Int): KeyCode = apply(n).fold(
    s => throw new IllegalArgumentException(s),
    v => v); 

  extension (p: KeyCode)
    def toInt: Int = p
    def next(mod: Int): KeyCode = KeyCode.unsafe((p.toInt + 1) % mod)
    def plusMod(other: KeyCode, mod: Int): KeyCode = KeyCode.unsafe((p + other) % mod)
    // def minusMod(other: KeyCode, mod: Int): KeyCode =
    //   val diff = p - other
    //   /* if diff is less than zero, then we must use the
    //      compliment of the mod to match the wheel markings */
    //   KeyCode.unsafe(if diff < 0 then (mod + diff) % mod else diff % mod)
  
  val zero = KeyCode.unsafe(0)

case class Translation private (size: Int, forward: Vector[KeyCode]):
  val reverse: Vector[KeyCode] = forward
    .zipWithIndex
    .sortBy(_._1.toInt)
    .map((n, idx) => KeyCode.unsafe(idx))

object Translation:
  def apply(size: Int, keyCodes: Vector[KeyCode]): Either[String, Translation] = keyCodes match
    case v if v.length != size => Left(s"Translation vector size (${v.length}) does not match given size ($size) values.")
    case v if v.length != v.distinct.length => Left(s"Translation vectors must not contain duplicate values.")
    case v if v.exists(n => n.toInt < 0 || n.toInt >= size) => Left(s"Translation vectors must contain only values from 0 to ${size-1}, inclusive.")
    case v => Right(new Translation(size = size, forward = v) {})

  def unsafe(s: String): Translation = ???

class ValidKeys(sz: Int, codes: Vector[KeyCode])

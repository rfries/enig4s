package org.somecode.enigma
package mach

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

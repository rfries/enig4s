package org.somecode.enigma
package mach

final case class Plugboard private (plugs: Map[Position, Position])
  extends Machine.Bus:
  val reverse = plugs.toList.map((k, v) => (v, k)).toMap

  def lookup(state: Machine.State, key: Position): Position =
    plugs.get(key).getOrElse(key)

  def reverseLookup(state: Machine.State, key: Position): Position =
    reverse.get(key).getOrElse(key)

object Plugboard:
  val MaxPlugs = Position.Max

  def apply(mappings: Set[String]): Either[String,Plugboard] =
    mappings.map(_.toUpperCase).toVector match
      case pairs if pairs.length > MaxPlugs =>
        Left(s"A plugboard may only have up to ${MaxPlugs} plugs specified.")
      case pairs if pairs.exists(_.length != 2) =>
        Left(s"Plug specifiers must have exactly two letters for each plug.")
      case pairs if pairs.exists(_.exists(c => c < 'A' || c >= 'Z')) =>
        Left(s"Plug specifiers must have exactly two letters for each plug.")

      case pairs =>
        val tupled = pairs.map(s => Position.unsafe(s(0)) -> Position.unsafe(s(1)))
        val c1 = tupled.map(_._1)
        val c2 = tupled.map(_._2)
        if (c1.length != c1.distinct.length || c2.length != c2.distinct.length)
          Left(s"Plug specifiers must not contain duplicate 'from' or 'to' components.")
        else
          Right(new Plugboard(tupled.toMap))


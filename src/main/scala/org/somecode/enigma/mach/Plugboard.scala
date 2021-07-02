package org.somecode.enigma
package mach

final case class Plugboard private (size: Int, forward: Map[KeyCode, KeyCode]):

  val reverse = forward.toList.map((k, v) => (v, k)).toMap
  val maxPlugs = size / 2

object Plugboard:

  def apply(max: Int, pairings: Map[KeyCode, KeyCode]): Either[String, Plugboard] =
    val values = pairings.values.toList
    val badKeys = (k: KeyCode) => k.toInt < 0 && k.toInt >= max
    pairings match
      case _ if max <= 0 => Left(s"Size ($max) must be positive.")
      case map if map.size > max / 2 => Left(s"This plugboard can have no more than ${max/2} pairings.")
      case map if map.keySet.exists(badKeys) || values.exists(badKeys) => Left(s"Key mappings must be >= 0 and < $max")
      case map =>
        val all = map.keySet.toList ++ values
        if all.size != all.distinct.size then
          Left("All pairings must be unique.")
        else if map.exists((k, v) => k.toInt == v.toInt) then
          Left("Pairings cannot map to themselves.")
        else Right(new Plugboard(max, map))

  def apply(max: Int, mappings: Set[String]): Either[String,Plugboard] =
    val badKeys = (k: KeyCode) => k.toInt < 'A' || k.toInt > 'Z'
    // if (size < 1) then Left(s"Size ($size) must be greater than 0.")
    // else if (size != mappings.size) then Left(s"")
    if (max < 1 || max > 26)
      Left("Letter codes are currently only supported up for 1 to 26 letters.")
    else if (mappings.exists(_.length != 2))
      Left(s"Plugboard pairing strings must have exactly two letters for each pairing.")
    else apply(max, mappings.map(s => KeyCode.unsafe(s(0).toInt) -> KeyCode.unsafe(s(1).toInt)).toMap)

    // mappings.map(_.toUpperCase).toVector match
    //   case pairs if pairs.length > max/2 =>
    //     Left(s"This plugboard may only have up to ${max/2} pairings specified.")
    //   case pairs if pairs.exists(_.length != 2) =>
    //     Left(s"Plugboard pairings must have exactly two letters for each plug.")
    //   case pairs if pairs.exists(_.exists(badkey) =>
    //     Left(s"Plug specifiers must have only letters A - Z.")
    //   case pairs =>
    //     val tupled = pairs.map(s => KeyCode.unsafe(s(0) - 'A') -> KeyCode.unsafe(s(1) - 'A'))
    //     val c1 = tupled.map(_._1)
    //     val c2 = tupled.map(_._2)
    //     if (c1.length != c1.distinct.length || c2.length != c2.distinct.length)
    //       Left(s"Plug specifiers must not contain duplicate 'from' or 'to' components.")
    //     else
    //       Right(new Plugboard(max, tupled.toMap))
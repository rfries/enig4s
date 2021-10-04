package org.somecode.enigma
package mach

final case class Plugboard private (size: Int, forward: Map[KeyCode, KeyCode]):

  val reverse = forward.toList.map((k, v) => (v, k)).toMap
  val maxPlugs = size / 2

object Plugboard:

  def maxSize = 26

  private def validSize(size: Int): Either[String, Int] =
    if (size < 1) then Left(s"Plugboard size ($size) must be greater than 0.")
    // else if (size != mappings.size) then Left(s"")
    else if (size > maxSize)
      Left(s"Plugboard size ($size) must be less than $maxSize.")
    else
      Right(size)

  private def validAscii(size: Int, mappings: Set[String]): Either[String, Map[KeyCode, KeyCode]] =
    if mappings.exists(_.length != 2) then
      Left(s"Plugboard pairing strings must have exactly two letters for each pairing.")
    else
      val ks = mappings.map(_.toUpperCase)
      if ks.flatMap(_.toCharArray).exists(c => c < 'A' || c > 'Z') then
        Left("String pairings for a Plugboard must contain only the letters 'A' through 'Z'.")
      else
        Right(ks.map(s => KeyCode.unsafe(s(0) - 'A') -> KeyCode.unsafe(s(1) - 'A')).toMap)

  private def validPairings(size: Int, pairings: Map[KeyCode, KeyCode]): Either[String, Map[KeyCode, KeyCode]] =
      val values = pairings.values.toList
      val badKeys = (k: KeyCode) => k < 0 && k >= size
      pairings match
        case map if map.size > size / 2 =>
          Left(s"This plugboard can have no more than ${size/2} pairings.")
        case map if map.keySet.exists(badKeys) || values.exists(badKeys) =>
          Left(s"Key mappings must be >= 0 and < $size")
        case map =>
          // Since we already know that the map keys are distinct, we
          // only need tocheck the values for distinctness.
          if values.size != values.distinct.size then
            Left("Pairings can not share sources or targets.")
          else if map.exists((k, v) => k.toInt == v.toInt) then
            Left("Pairings cannot map to themselves.")
          else
            Right(map)

  def apply(size: Int, pairings: Map[KeyCode, KeyCode]): Either[String, Plugboard] =
    for
      sz <- validSize(size)
      map <- validPairings(size, pairings)
    yield new Plugboard(sz, map)

  def apply(size: Int, mappings: Set[String]): Either[String, Plugboard] =
    for
      sz <- validSize(size)
      pairings <- validAscii(sz, mappings)
      map <- validPairings(sz, pairings)
    yield
      new Plugboard(sz, map)

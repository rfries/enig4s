package org.somecode.enig4s
package mach

import scala.util.Left
import org.scalatest.AppendedClues
import org.scalatest.EitherValues.*
import org.scalatest.matchers.should
import org.scalatest.wordspec.AnyWordSpec

object WiringSpecFixtures:
  val goodWiring: Vector[Wiring] = Vector(
    Wiring(SymbolMap.AZ.stringToCodes("ZABCDEFGHIJKLMNOPQRSTUVWXY").value).value,
    Wiring(SymbolMap.AZ.stringToCodes("BCDEFGHIJKLMNOPQRSTUVWXYZA").value).value,
    Wiring(SymbolMap.AZ.stringToCodes("ABCDE").value).value
  )

  val goodWiringStrings: Vector[String] = Vector(
    "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
    "ABCDFEGHIJKLMNPOQRSTUVWXYZ", // O and P swapped
    "ZABCDEFGHIJKLMNOPQRSTUVWXY", // rotated right one position
    "A"                           // any single character should be legal
  )

  val badWiringStrings: Vector[String] = Vector(
    "",                           // empty
    "ADEFGHIJKLMNOPQRSTUVWXYZ",   // not continuous
    "ABBDEFGHIJKLMNOPQRSTUVWXYZ"  // duplicate
  )

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
    "ABCDFEGHIJKLMNPOQRSTUVWXYZ",
    "ZABCDEFGHIJKLMNOPQRSTUVWXY",
    "A"
  )

  val badWiringStrings: Vector[String] = Vector(
    "ADEFGHIJKLMNOPQRSTUVWXYZ",
    "ABCDZ",
    "ABBDEFGHIJKLMNOPQRSTUVWXYZ"
  )

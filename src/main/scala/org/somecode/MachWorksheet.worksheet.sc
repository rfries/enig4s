import org.somecode.enigma.mach.*
import cats.*

import Rotor._

val r: Option[Name] = Name("hello").toOption

val rotor = Rotor("f1", "ZABCDEFGHIJKLMNOPQRSTUVWXY", Set(5), 1)

rotor

val p1 = Position.unsafe(3)

p1.toString
//summon[Show[Position]].show(p1)

3.toString

val a = Value(2)
val b = Value(29)
val c = a match
  case Left(s) => throw new RuntimeException("bad a")
  case Right(v) => v.copy(v = 29)


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

val a = Position(24)
val b = Position(25)
// val c = a match
//   case Left(s) => throw new RuntimeException("bad a")
//   case Right(v) => v + Value(29)
for
  va <- a
  vb <- b
yield
  (va + vb, va - vb)


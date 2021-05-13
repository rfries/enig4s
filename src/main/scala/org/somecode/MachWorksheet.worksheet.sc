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

val a = Value(1)
val b = Value(25)
// val c = a match
//   case Left(s) => throw new RuntimeException("bad a")
//   case Right(v) => v + Value(29)
for
  va <- a
  vb <- b
yield
  (va + vb, va - vb)


val n = 5

val abs: Int => Int = Math.abs

2 + 2*n % n

2 - 14 % n

-6 % 5
abs(-11 % 5)

-11 % 6





(3-1) - Math.abs((1-3) % 3)

Math.abs((1-4) % 3)

  
Math.abs(3-13) % 3

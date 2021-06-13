import org.somecode.enigma.mach.*
//import org.somecode.enigma.mach.Wiring
//import cats.*

val wheel = Wheel("ZABCDEFGHIJKLMNOPQRSTUVWXY", Position.unsafe(1), Set(Position.unsafe(5)))
  .toOption.getOrElse(throw new IllegalArgumentException("bad wheel"))

val x = 3
val y = 4
x + y

Wiring.fromString("ZABCDEFGHIJKLMNOPQRSTUVWXY")

val p1  = Position.unsafe(1)
val p7  = Position.unsafe(7)
val p22 = Position.unsafe(22)

0 < p1
7 > p1
7 > p7
7 > p22

p1.toString

val a = Position(24)
val b = Position(25)

val c = Position.unsafe(24)
val d = Position.unsafe(25)

c.+(d)

a

// val c = a match
//   case Left(s) => throw new RuntimeException("bad a")
//   case Right(v) => v + Value(29)
for
  va: Position <- a
  vb: Position <- b
yield
  (va + vb, va - vb)


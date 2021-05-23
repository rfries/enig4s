import org.somecode.enigma.mach.*
import cats.*

val wheel = Wheel("ZABCDEFGHIJKLMNOPQRSTUVWXY", Position.unsafe(1), Set(Position.unsafe(5)))
  .toOption.getOrElse(throw new IllegalArgumentException("bad wheel"))

//p1.toString
//summon[Show[Position]].show(p1)

3.toString

wheel.lookup

val a = Position(24)
val b = Position(25)
val p1 = Position.unsafe(3)
// val c = a match
//   case Left(s) => throw new RuntimeException("bad a")
//   case Right(v) => v + Value(29)
for
  va <- a
  vb <- b
yield
  (va + vb, va - vb)


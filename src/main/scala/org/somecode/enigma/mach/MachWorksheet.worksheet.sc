import org.somecode.enigma.mach.*
import org.somecode.enigma.mach.KeyCode.*
//import org.somecode.enigma.mach.Wiring
//import cats.*

val wheel = Wheel("ZABCDEFGHIJKLMNOPQRSTUVWXY", KeyCode.unsafe(1), Set(KeyCode.unsafe(5)))
  .toOption.getOrElse(throw new IllegalArgumentException("bad wheel"))

val x = 3
val y = 5
x + y

Wiring.fromString("ZABCDEFGHIJKLMNOPQRSTUVWXY")

val p1  = KeyCode.unsafe(1)
val p7  = KeyCode.unsafe(7)
val p22 = KeyCode.unsafe(22)

// 0 < p1.toInt
// 7 > p1.toInt
// 7 > p7.toInt
// 7 > p22.toInt

p1.toString

val a = KeyCode(24)
val b = KeyCode(25)

val c = KeyCode.unsafe(24)
val d = KeyCode.unsafe(25)

c.+(d)

a

// val c = a match
//   case Left(s) => throw new RuntimeException("bad a")
//   case Right(v) => v + Value(29)
for
  va: KeyCode <- a
  vb: KeyCode <- b
yield
  (va + vb, va - vb)


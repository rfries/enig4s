import org.somecode.enigma.mach.*
import org.somecode.enigma.mach.KeyCode.*
//import org.somecode.enigma.mach.Wiring
//import cats.*

Wiring.fromString("ZABCDEFGHIJKLMNOPQRSTUVWXY").map {
  wiring =>
    val wheel = Wheel(wiring, KeyCode.unsafe(1), Set(KeyCode.unsafe(5)))
      .getOrElse(throw new IllegalArgumentException("bad wheel"))
    wheel
}

val k1: KeyCode = KeyCode.unsafe(3)
val k2: KeyCode = KeyCode.unsafe(0)

val v1 = k1
val v2: KeyCode = k1
val v3: Int = k1

val x = 3
val y = 5
x + y

//val wiring = Wiring.fromString("ZABCDEFGHIJKLMNOPQRSTUVWXY")

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


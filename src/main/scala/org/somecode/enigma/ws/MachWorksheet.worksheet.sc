

import org.somecode.enigma.mach.*
import org.somecode.enigma.mach.KeyCode.*
import org.somecode.enigma.mach.Machine.*
//import org.somecode.enigma.mach.Wiring
import cats.*
import cats.data.*

val wheel: Either[String, ConfiguredWheel] =
  Wheel("ZABCDEFGHIJKLMNOPQRSTUVWXY", Set("E"))
    .flatMap(_.configure(KeyCode.unsafe(0)))

wheel match
  case Left(msg) =>
    println("*** bad wheel")
  case Right(wheel) =>
    println(">>> good wheel")

    val pos = KeyCode.unsafe(0)
    val in = KeyCode.unsafe(20)

    type WState = State[WheelState, Unit]
    val adv: WState = State { wstate =>
      if (wstate.atNotch || wstate.rightNotch)
        (wstate.copy(position = wstate.position.next(wheel.size)), ())
      else
        (wstate, ())
    }

/*

val k1: KeyCode = KeyCode.unsafe(3)
val k2: KeyCode = KeyCode.unsafe(0)

val v1 = k1
val v2: KeyCode = k1
val v3: Int = k1

val x = 3
val y = 5
x + y

//val wiring = Wiring.fromString("ZABCDEFGHIJKLMNOPQRSTUVWXY")

val a = KeyCode(24)
val b = KeyCode(25)

val c = KeyCode.unsafe(24)
val d = KeyCode.unsafe(25)

for
  va: KeyCode <- a
  vb: KeyCode <- b
yield
  (va + vb, va - vb)
*/

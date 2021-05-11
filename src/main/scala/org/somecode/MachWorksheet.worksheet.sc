import org.somecode.enigma.mach.*
import cats.*

import Rotor._

val r: Option[Name] = Name("hello").toOption

val rotor = Rotor("f1", "ZABCDEFGHIJKLMNOPQRSTUVWXY", Vector(5), 1)

rotor

val p1 = Position.unsafe(3)

p1.toString
//summon[Show[Position]].show(p1)

3.toString

3+61

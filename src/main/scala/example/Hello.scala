package example

import org.somecode.enigma.mach.*
import cats.*

object Hello extends Greeting with Run with App {
  println(greeting)
  run
}

trait Greeting {
  lazy val greeting: String = "hello"
}

trait Run:
  def run: Unit =
    println("Hello!")
    // Wheel.validate(Wiring.unsafe("ZABCDEFGHIJKLMNOPQRSTUVWXY"), KeyCode.unsafe(1), Set(KeyCode.unsafe(5))) match
    // case Left(s) => throw new RuntimeException(s)
    // case Right(wheel) =>
    //   println(wheel.toString)
    //   println(wheel.ringSetting)
      //summon[Show[Position]].show(rotor.ringSetting)
end Run
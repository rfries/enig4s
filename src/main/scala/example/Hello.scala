package example

import org.somecode.enigma.mach.*
import cats.*

import Rotor._


object Hello extends Greeting with Run with App {
  println(greeting)
  run
}

trait Greeting {
  lazy val greeting: String = "hello"
}

trait Run:
  def run: Unit =
    Rotor("f1", "ZABCDEFGHIJKLMNOPQRSTUVWXY", Vector(5), 0x1) match
    case Left(s) => throw new RuntimeException(s)
    case Right(rotor) =>
      println(rotor.toString)
      println(rotor.ringSetting.value)
      //summon[Show[Position]].show(rotor.ringSetting)
end Run
import cats.*
import cats.data.State
import org.somecode.enigma.mach.KeyCode

case class SomeState(n: Int)

val addOne: State[SomeState, Int] = State { (s: SomeState) =>
  val nn = s.n + 1
  (SomeState(nn), nn)
}

addOne.run(SomeState(1))
//case class WheelState(pos: KeyCode)
//def advance(state: WheelState, pos: KeyCode): WheelState = ???

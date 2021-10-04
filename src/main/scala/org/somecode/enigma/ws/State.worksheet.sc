import cats.*
import cats.data.State
import org.somecode.enigma.mach.KeyCode

case class SomeState(n: Int)

val addOne: SomeState => (SomeState, Int) = (s: SomeState) =>
  val nn = s.n + 1
  (SomeState(nn), nn)
  

// val addOne: State[SomeState, Int] = State { (s: SomeState) =>
//   val nn = s.n + 1
//   (SomeState(nn), nn)
// }

val ls = List(3,100,1000)

State[SomeState,Int].apply

//ls.map(SomeState.apply).map(addOne)


//addOne.run(SomeState(1))
//case class WheelState(pos: KeyCode)
//def advance(state: WheelState, pos: KeyCode): WheelState = ???

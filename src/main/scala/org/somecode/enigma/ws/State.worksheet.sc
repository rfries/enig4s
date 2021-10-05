import cats.*
import cats.data.State
import org.somecode.enigma.mach.KeyCode

case class SomeState(n: Int)

val addOne: SomeState => (SomeState, Int) = (s: SomeState) =>
  val nn = s.n + 1
  (SomeState(nn), nn)

def addToList(ns: Vector[Int]): Vector[(SomeState, Int)] =
  def next(state: SomeState, ns: Vector[Int], out: Vector[(SomeState, Int)]): Vector[(SomeState, Int)] =
    ns match
      case n +: remaining =>
          val (newState, nn) = addOne(state)
          next(newState, remaining, out :+ (newState, nn))
      case _ => out
  next(SomeState(0), ns, Vector.empty)

// val addOne: State[SomeState, Int] = State { (s: SomeState) =>
//   val nn = s.n + 1
//   (SomeState(nn), nn)
// }

val ls = List(3,100,1000)

State[SomeState,Int].apply(addOne)
State.apply(addOne)

ls.map(SomeState.apply).map(addOne)

// def crypt(c: Char): Char = (c ^ '\u0037').asInstanceOf[Char]
// val cs = "Hello There".map(crypt).map(crypt)



//addOne.run(SomeState(1))
//case class WheelState(pos: KeyCode)
//def advance(state: WheelState, pos: KeyCode): WheelState = ???

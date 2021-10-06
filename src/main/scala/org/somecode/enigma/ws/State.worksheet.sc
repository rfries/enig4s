import scala.annotation.tailrec
import cats.*
import cats.data.State
import org.somecode.enigma.mach.KeyCode

case class SomeState(n: Int)


val addOne: SomeState => (SomeState, Unit) = (s: SomeState) =>
  val nn = s.n + 1
  (SomeState(s.n + 1), ())

def crypt(state: SomeState, c: Char): Char = (c ^ state.n).toChar
def crypt(init: SomeState, text: Vector[Char]): Vector[(SomeState, Char)] =
  @tailrec
  def next(state: SomeState, in: Vector[Char], out: Vector[(SomeState, Char)]): Vector[(SomeState, Char)] =
    in match
      case c +: remaining =>
          val (newState, _) = addOne(state)
          next(newState, remaining, out :+ (newState, crypt(newState, c)))
      case _ => out
  next(init, text, Vector.empty)

def crypt(state: SomeState, text: String): String =
  String(crypt(state, text.toVector).toArray.map(_._2))

crypt(SomeState(0), crypt(SomeState(0), "Hello There!"))


// val addOne: State[SomeState, Int] = State { (s: SomeState) =>
//   val nn = s.n + 1
//   (SomeState(nn), nn)
// }

val ls = List(3,100,1000)

State[SomeState,Unit].apply(addOne)
State.apply(addOne)

ls.map(SomeState.apply).map(addOne)

// def crypt(c: Char): Char = (c ^ '\u0037').asInstanceOf[Char]
// val cs = "Hello There".map(crypt).map(crypt)



//addOne.run(SomeState(1))
//case class WheelState(pos: KeyCode)
//def advance(state: WheelState, pos: KeyCode): WheelState = ???

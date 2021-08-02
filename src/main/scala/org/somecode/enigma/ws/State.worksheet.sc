import cats.*
import cats.data.State
import org.somecode.enigma.mach.KeyCode

case class WheelState(pos: KeyCode)

def advance(state: WheelState, pos: KeyCode): WheelState = ???

import org.somecode.enig4s.mach.Machine.WheelState
import org.somecode.enig4s.mach.Machine.MachineState
import org.somecode.enig4s.mach.*

val wheels = Vector(
  //Wheels.III.configure(KeyCode.unsafe(2)).require,
  Wheels.III.configure(KeyCode.zero).require,
  Wheels.II.configure(KeyCode.zero).require,
  Wheels.I.configure(KeyCode.zero).require
)

val reflector = Reflector(Wirings.B).require
val mstate = MachineState(
  Vector(
    WheelState(KeyCode.zero),
    WheelState(KeyCode.zero),
    WheelState(KeyCode.zero)
  ),
  WheelState(KeyCode.zero)
)

val c = 'C'

c - 'A'
23


Machine(wheels, reflector, Wirings.ETW) match
  case Right(mach) =>
    mach.crypt(mstate, ValidKeys("AAAAA").require)
  case _ => throw new RuntimeException("boo!")

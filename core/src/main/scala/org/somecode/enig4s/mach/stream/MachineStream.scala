package org.somecode.enig4s
package mach
package stream

import fs2.Stream
import org.somecode.enig4s.mach.Machine.MachineState

object MachineStream:
  def stream[F[_]](mach: Machine, state: MachineState, in: fs2.Stream[F, Int]): fs2.Stream[F, Int] =
    ???
    // in.map(KeyCode.unsafe)
    //   .scan((mach, state)) { case  ((mach, state), c) =>
    //     mach.crypt(state, c)
    // }

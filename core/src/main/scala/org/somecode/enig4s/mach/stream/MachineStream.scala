package org.somecode.enig4s
package mach
package stream

import fs2.Stream
import cats.effect.IO
import cats.effect.SyncIO

object MachineStream:
  def stream[F[_]](mach: Machine, initialState: MachineState, in: fs2.Stream[F, Int]): fs2.Stream[F, (MachineState, KeyCode)] =
    // in.evalMapAccumulate(initialState) { (state, in) =>
    //   mach.crypt(state, in) match
    //     case Right(t @ (newState, out)) => SyncIO.pure(t)
    //     case Left(msg) => IO.raiseError(new IllegalArgumentException(msg))
    // }
    ???

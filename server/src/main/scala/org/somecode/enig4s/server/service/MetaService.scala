package org.somecode.enig4s
package server
package service

import cats.effect._
import cats.implicits._
import fs2.concurrent.SignallingRef
import fs2.Stream
import org.http4s.HttpRoutes
import org.http4s.dsl.*
import org.http4s.implicits.*

import scala.concurrent.duration.*

object MetaService:

  def routes[F[_]: Async](shutdown: SignallingRef[F, Boolean]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "ok" =>
        Ok(s"OK ${BuildInfo.name} ${BuildInfo.version}\n")
      case req @ GET -> Root / "stream" =>
        Ok(produce(1.second, 5){ case (d, i) => s"$i: $d\n"})
      case POST -> Root / "shutdown" =>
        for
          _ <- shutdown.set(true)
          res <- Ok()
        yield res
    }
  }

  def produce[F[_]: Temporal, A](delay: FiniteDuration, count: Long)(f: ((FiniteDuration, Long)) => A): Stream[F, A] =
    Stream.awakeEvery[F](delay)
      .zipWithIndex
      .evalTap {
        case (d, i) => Temporal[F].pure { println(s"$i: $d") } // shouldn't work (needs delay)... upgrading http4s WIP
      }
      .map(f)
      .take(count)

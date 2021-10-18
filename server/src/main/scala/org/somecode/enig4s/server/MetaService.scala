package org.somecode.enig4s.server

import cats.effect._
import fs2.Stream
import org.http4s._
import org.http4s.dsl._
import scala.concurrent.duration._

object MetaService {

  def routes[F[_]: Async]: HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "ok" => Ok("enig4s http ok\n")
      case GET -> Root / "stream" => Ok(produce(1.second, 5){ case (d, i) => s"$i: $d\n"})
      case GET -> Root / "seconds" => Status.Ok(seconds.map(_.toString))
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

  def seconds[F[_]: Temporal]: Stream[F, FiniteDuration] = Stream.awakeEvery[F](1.second)
}

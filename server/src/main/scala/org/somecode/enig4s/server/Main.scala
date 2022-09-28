package org.somecode.enig4s.server

import cats.effect.*
import cats.implicits.*
import com.comcast.ip4s._
import fs2.concurrent.SignallingRef
import fs2.Stream
import org.http4s.HttpRoutes
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits.*
import org.http4s.server.Router
import org.http4s.server.staticcontent.{FileService, fileService}
import org.somecode.enig4s.mach.Cabinet
import org.somecode.enig4s.server.service.MachineService
import org.somecode.enig4s.server.service.MetaService

import scala.concurrent.ExecutionContext.global

object Main extends IOApp:

  override def run(args: List[String]): IO[ExitCode] =
    serverStream[IO].compile.lastOrError

  private[server] def serverStream[F[_]: Async]: Stream[F, ExitCode] =
    for
      // create the shutdown signal and exit status ref (success if not changed)
      shutdown  <- Stream.eval(SignallingRef[F, Boolean](false))
      exitCode  <- Stream.eval(Ref[F].of(ExitCode.Success))
      // initialize pre-defined objects
      cabinet   <- Stream.fromEither(Cabinet().left.map(s => new java.lang.IllegalArgumentException(s)))
      // create the server
      serverRes = Stream.resource(EmberServerBuilder
                    .default[F]
                    .withHost(ipv4"0.0.0.0")
                    .withPort(port"8080")
                    .withHttpApp(routes[F](shutdown, cabinet).orNotFound)
                    .build)
      // emulate serveWhile from BlazeServerBuilder (so endpoint can signal shutdown)
      server    <- serverRes
                    *> (shutdown.discrete.takeWhile(_ === false).drain
                      ++ Stream.eval(exitCode.get))
    yield server

  private[server] def routes[F[_]: Async](shutdown: SignallingRef[F, Boolean], cabinet: Cabinet): HttpRoutes[F] =
    Router(
      "files"   ->  fileService[F](FileService.Config[F]("./static")),
      "enigma"  ->  MachineService(cabinet).routes,
      "meta"    ->  MetaService.routes(shutdown)
    )

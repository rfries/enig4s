package org.somecode.enig4s.server

import cats.effect.*
import org.http4s.HttpRoutes
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.implicits.*
import org.http4s.server.Router
import org.http4s.server.staticcontent.{FileService, fileService}
import org.http4s.server.websocket.WebSocketBuilder2
import fs2.concurrent.SignallingRef
import fs2.Stream
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
      cabinet   <- Stream.fromEither(Cabinet.init().left.map(s => new java.lang.IllegalArgumentException(s)))
      // create the server
      ret       <- BlazeServerBuilder[F]
                    .bindHttp(8080, "0.0.0.0")
                    .withHttpApp(routes[F](shutdown, cabinet).orNotFound)
                    .serveWhile(shutdown, exitCode)
    yield ret

  private[server] def routes[F[_]: Async](shutdown: SignallingRef[F, Boolean], cabinet: Cabinet): HttpRoutes[F] =
    Router(
      "files"   ->  fileService[F](FileService.Config[F]("./static")),
      "enigma"    ->  MachineService(cabinet).routes,
      "meta"    ->  MetaService.routes(shutdown)
    )

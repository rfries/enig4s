package org.somecode.enig4s.server

import cats.effect.*
import cats.effect.std.Queue
import fs2.Stream
import org.http4s.HttpRoutes
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.implicits.*
import org.http4s.server.Router
import org.http4s.server.staticcontent.{FileService, fileService}
import org.http4s.server.websocket.WebSocketBuilder2

import scala.concurrent.ExecutionContext.global

object Main extends IOApp:

  override def run(args: List[String]): IO[ExitCode] =
    serverStream[IO].compile.lastOrError

  private[server] def serverStream[F[_]: Async]: Stream[F, ExitCode] =
    for
      ret <-  BlazeServerBuilder[F]
                .bindHttp(8080, "0.0.0.0")
                .withHttpApp(routes[F].orNotFound)
                .serve
    yield
      ret

  private[server] def routes[F[_]: Async]: HttpRoutes[F] =
    Router(
      "files"     ->    fileService[F](FileService.Config[F]("./static")),
      "mach"      ->    MachineService.routes,
      "meta"      ->    MetaService.routes
    )

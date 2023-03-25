package org.somecode.enig4s.server

import cats.effect.*
import cats.effect.implicits.*
import cats.implicits.*
import com.comcast.ip4s.*
import fs2.concurrent.SignallingRef
import fs2.Stream
import org.http4s.HttpRoutes
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits.*
import org.http4s.server.{Router, Server}
import org.http4s.server.staticcontent.{FileService, fileService}
import org.somecode.enig4s.mach.Cabinet
import org.somecode.enig4s.server.service.MachineService
import org.somecode.enig4s.server.service.MetaService
import cats.Applicative

object Main extends ResourceApp:

  override def run(args: List[String]): Resource[IO, ExitCode] =
    for
      shutdown <- serverResource[IO]
      _ <- shutdownResource(shutdown)
    yield ExitCode.Success

  def cabResource[F[_]](using F: Async[F]): Resource[F, Cabinet] =
    Resource.liftK(F.fromEither(Cabinet().leftMap(s => new IllegalStateException(s))))

  def shutdownResource[F[_]: Async](ref: SignallingRef[F, Boolean]): Resource[F, Option[Boolean]] =
    ref.discrete.takeWhile(_ === false).drain.compile.resource.last

  def serverResource[F[_]: Async]: Resource[F, SignallingRef[F, Boolean]] =
    for
      shutdown  <- Resource.liftK(SignallingRef[F, Boolean](false))
      cabinet   <- cabResource
      _ <- EmberServerBuilder
        .default[F]
        .withHost(ipv4"0.0.0.0")
        .withPort(port"8080")
        .withHttpApp(routes[F](shutdown, cabinet).orNotFound)
        .build
    yield shutdown

  private[server] def routes[F[_]: Async](shutdown: SignallingRef[F, Boolean], cabinet: Cabinet): HttpRoutes[F] =
    Router(
      "files"   ->  fileService[F](FileService.Config[F]("./static")),
      "enigma"  ->  MachineService(cabinet).routes,
      "meta"    ->  MetaService.routes(shutdown)
    )

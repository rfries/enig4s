package org.somecode.enig4s.server

import cats.effect.*
import cats.effect.implicits.*
import cats.implicits.*
import com.comcast.ip4s.*
import fs2.concurrent.SignallingRef
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.HttpRoutes
import org.http4s.server.Router
import org.http4s.server.staticcontent.{FileService, fileService}
import org.somecode.enig4s.mach.Cabinet
import org.somecode.enig4s.server.service.MachineService
import org.somecode.enig4s.server.service.MetaService

import java.time.Instant
import scala.concurrent.duration.*

object Main extends ResourceApp.Simple:

  override def run: Resource[IO, Unit] =
    serverResource[IO].flatMap(signal => shutdownResource(signal))

  def serverResource[F[_]](using F: Async[F]): Resource[F, SignallingRef[F, Boolean]] =
    for
      shutdown    <- Resource.eval(SignallingRef[F, Boolean].apply(false))
      cabinet     <- cabinetResource
      streamerSvc <- Resource.eval(StreamerService.localMapService)
      _           <- fs2.Stream
                      .awakeEvery(5.seconds)
                      .evalMap(_ => streamerSvc.expire(Instant.now()))
                      .compile
                      .resource
                      .drain

      _           <- EmberServerBuilder
                      .default[F]
                      .withHost(ipv4"0.0.0.0")
                      .withPort(port"8080")
                      .withHttpApp(routes[F](shutdown, cabinet, streamerSvc).orNotFound)
                      .build
    yield shutdown

  def cabinetResource[F[_]](using F: Async[F]): Resource[F, Cabinet] =
    Resource.eval(F.fromEither(Cabinet().leftMap(s => new IllegalStateException(s))))

  def shutdownResource[F[_]: Async](ref: SignallingRef[F, Boolean]): Resource[F, Unit] =
    ref.discrete.takeWhile(_ === false).compile.resource.drain

  private[server] def routes[F[_]: Async](shutdown: SignallingRef[F, Boolean], cabinet: Cabinet, streamerSvc: StreamerService[F]): HttpRoutes[F] =
    Router(
      "files"   ->  fileService[F](FileService.Config[F]("./static")),
      "enigma"  ->  MachineService(cabinet, streamerSvc).routes,
      "meta"    ->  MetaService.routes(shutdown)
    )

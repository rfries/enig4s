package org.somecode.enig4s
package server
package service

import cats.effect.Concurrent
import cats.implicits.*
import org.http4s.HttpRoutes
import org.http4s.EntityDecoder
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.somecode.enig4s.server.api.MachineRequest
import org.http4s.Response

object MachineService:

  def routes[F[_]: Concurrent]: HttpRoutes[F] = {
    given EntityDecoder[F, MachineRequest] = jsonOf[F, MachineRequest]
    val dsl = new Http4sDsl[F] {}
    import dsl.*
    HttpRoutes.of[F] {
      case req @ POST -> Root =>
        for
          mreq <- req.as[MachineRequest]
          resp <- Ok()
        yield resp

      case GET -> Root / "wheels" =>
        Ok("Ok")
    }
  }

  def machineRequest[F[_]: Concurrent](mreq: MachineRequest): F[Response[F]] =
    val dsl = new Http4sDsl[F] {}
    import dsl.*
    Ok()

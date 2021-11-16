package org.somecode.enig4s
package server
package service

import cats.effect.Concurrent
import cats.implicits.*
import io.circe.*
import io.circe.syntax.*
import io.circe.generic.auto.*
import org.http4s.*
import org.http4s.circe.CirceEntityCodec.*
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits.*
import org.somecode.enig4s.jsapi.{MachineRequest, MachineResponse}

class MachineService[F[_]](using F: Concurrent[F]) extends Http4sDsl[F]:

  def routes: HttpRoutes[F] = {
    HttpRoutes.of[F] {
      case req @ POST -> Root =>
        for
          mreq <- req.as[MachineRequest]
          resp <- machineRequest(mreq)
        yield resp

      case GET -> Root / "wheels" =>
        Ok("Ok")
    }
  }

  def machineRequest(mreq: MachineRequest): F[Response[F]] =
    Ok(MachineResponse("AAA", "SENDMORECHUCKBERRY").asJson)

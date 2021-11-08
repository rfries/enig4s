package org.somecode.enig4s
package server
package service

import cats.effect.Concurrent
import cats.effect.Async
import cats.implicits.*
import io.circe.*
import io.circe.syntax.*
import io.circe.generic.auto.*
import org.http4s.*
import org.http4s.circe.CirceEntityCodec.*
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits.*
import org.somecode.enig4s.server.api.{MachineRequest, MachineResponse}

class MachineService[F[_]](using F: Async[F]) extends Http4sDsl[F]:

  def routes: HttpRoutes[F] = {
    HttpRoutes.of[F] {
      case req @ POST -> Root =>
        for
          mreq <- req.as[MachineRequest]
          resp <- Ok(MachineResponse("AAA", "SENDMORECHUCKBERRY").asJson)
          //resp <- Ok(json"""{ "hello": "there" }""")
        yield resp

      case GET -> Root / "wheels" =>
        Ok("Ok")
    }
  }

  def machineRequest(mreq: MachineRequest): F[Response[F]] =
    Ok()

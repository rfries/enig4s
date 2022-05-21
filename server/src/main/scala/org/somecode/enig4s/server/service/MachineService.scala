package org.somecode.enig4s
package server
package service

import cats.effect.Concurrent
import cats.implicits.*
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.http4s.circe.CirceEntityCodec.*
import org.http4s.dsl.Http4sDsl
import org.somecode.enig4s.jsapi.MachineRequest
import org.somecode.enig4s.jsapi.MachineResponse
import org.somecode.enig4s.mach.Cabinet
import org.http4s.HttpRoutes
import org.http4s.Response
import org.somecode.enig4s.jsapi.MachineRequestJs

class MachineService[F[_]](cabinet: Cabinet)(using F: Concurrent[F]) extends Http4sDsl[F]:

  def routes: HttpRoutes[F] = {
    HttpRoutes.of[F] {
      case req @ POST -> Root =>
        for
          mreq <- req.as[MachineRequestJs]
          resp <- machineRequest(mreq)
        yield resp

      case GET -> Root / "wheels" =>
        //Ok(cabinet.wheels.asJson)
        Ok("Ok")
    }
  }

  def machineRequest(mreq: MachineRequestJs): F[Response[F]] =
    Ok(MachineResponse("AAA", "SENDMORECHUCKBERRY").asJson)

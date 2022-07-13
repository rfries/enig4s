package org.somecode.enig4s
package server
package service

import cats.effect.Concurrent
import cats.implicits.*
import io.circe.Json
import io.circe.syntax.*
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec.*
import org.somecode.enig4s.jsapi.MachineRequestJs
import org.somecode.enig4s.jsapi.MachineResponse
import org.somecode.enig4s.mach.Cabinet

class MachineService[F[_]](cabinet: Cabinet)(using F: Concurrent[F]) extends Enig4sService[F]:

  def routes: HttpRoutes[F] = {
    HttpRoutes.of[F] {
      case req @ POST -> Root =>
        jsonResponseEither[MachineRequestJs](req) {
          mreqJs =>
            for
              mreq <- mreqJs.toMachineRequest(cabinet)
              mstate <- mreq.machine.ValidState(mreq.state)
              out <- mreq.machine.crypt(mstate.state, mreq.text, trace = false)
              resp = MachineResponse(out.state.readable(mreq.machine.symbols), out.text)
            yield resp.asJson
        }

      case GET -> Root / "wheels" =>
        Ok(Json.arr(cabinet.wheels.keys.map(Json.fromString).toSeq *))

      case GET -> Root / "reflectors" =>
        Ok(Json.arr(cabinet.reflectors.keys.map(Json.fromString).toSeq *))

      case GET -> Root / "symbolmaps" =>
        Ok(Json.arr(cabinet.symbols.keys.map(Json.fromString).toSeq *))

      case GET -> Root / "wirings" =>
        Ok(Json.arr(cabinet.wirings.keys.map(Json.fromString).toSeq *))
    }
  }

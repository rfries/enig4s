package org.somecode.enig4s
package server
package service

import cats.effect.Concurrent
import cats.implicits.*
import io.circe.Encoder
import io.circe.Json
import io.circe.generic.semiauto.*
import io.circe.syntax.*
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec.*
import org.somecode.enig4s.jsapi.CabinetJs.given
import org.somecode.enig4s.jsapi.MachineRequestJs
import org.somecode.enig4s.jsapi.MachineResponse
import org.somecode.enig4s.mach.Cabinet

class MachineService[F[_]](cabinet: Cabinet)(using F: Concurrent[F]) extends Enig4sService[F]:

  def routes: HttpRoutes[F] = {
    HttpRoutes.of[F] {
      case req @ POST -> Root =>
        jsonResponseEither[MachineRequestJs](req) {
          (mreqJs: MachineRequestJs) =>
            for
              mreq <- mreqJs.toMachineRequest(cabinet)
              mstate <- mreq.machine.ValidState(mreq.state)
              out <- mreq.machine.crypt(mstate.state, mreq.text)
              resp = MachineResponse(out.text, out.state.displayPositions(mreq.machine.symbols), out.trace)
            yield resp.asJson
        }

      case GET -> Root / "wheels" =>
        Ok(cabinet.wheelInits.asJson)

      case GET -> Root / "reflectors" =>
        Ok(cabinet.reflectorInits.asJson)

      case GET -> Root / "symbolmaps" =>
        Ok(cabinet.symbolInits.asJson)

      case GET -> Root / "wirings" =>
        Ok(cabinet.wiringInits.asJson)
    }
  }

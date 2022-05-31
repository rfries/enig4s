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
import cats.effect.SyncIO

class MachineService[F[_]](cabinet: Cabinet)(using F: Concurrent[F]) extends Http4sDsl[F]:

  def routes: HttpRoutes[F] = {
    HttpRoutes.of[F] {
      case req @ POST -> Root =>
        //val mreqJs = req.as[MachineRequestJs].fold

        // for
        //   mreqJs <- req.as[MachineRequestJs]
        // yield for
        //   mreq <- mreqJs.toMachineRequest(cabinet)
        //   mstate <- mreq.machine.ValidState(mreq.state)
        //   out <- mreq.machine.crypt(mstate.state, mreq.text)
        //   resp = MachineResponse(out._1.wheelPositions(mreq.machine.symbols), out._2)
        //   respJs = resp.asJson
        // yield respJs
        //   yield out
        // yield out) match
        //   case Left(s) => BadRequest(s)
        //   case Right(s) => Ok(s)

        val ret = Ok("Boo!")
        ret

      case GET -> Root / "wheels" =>
        //Ok(cabinet.wheels.asJson)
        Ok("Ok")

      case GET -> Root / "reflectors" =>
        //Ok(cabinet.wheels.asJson)
        Ok("Ok")

      case GET -> Root / "wirings" =>
        //Ok(cabinet.wheels.asJson)
        Ok("Ok")
    }
  }

  def machineRequest(mreq: MachineRequestJs): F[Response[F]] =
    Ok(MachineResponse("AAA", "SENDMORECHUCKBERRY").asJson)

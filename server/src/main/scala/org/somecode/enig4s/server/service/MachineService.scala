package org.somecode.enig4s
package server
package service

import cats.effect.kernel.Async
import cats.effect.{Concurrent, Sync, SyncIO}
import cats.implicits.*
import fs2.Stream
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

import java.time.Instant

class MachineService[F[_]](cabinet: Cabinet, streamerSvc: StreamerService[F])(using F: Async[F]) extends Enig4sService[F]:

  def routes: HttpRoutes[F] = {
    HttpRoutes.of[F] {
      case req @ POST -> Root =>
        jsonResponseEither[MachineRequestJs](req) {
          (mreqJs: MachineRequestJs) =>
            for
              mreq <- mreqJs.toMachineRequest(cabinet)
              mstate <- mreq.machine.ValidState(mreq.state)
              out <- mreq.machine.crypt(mstate.state, mreq.text)
              trc = if mreq.state.trace then out.trace else None
              resp = MachineResponse(out.text, out.state.displayPositions(mreq.machine.symbols), trc)
            yield resp.asJson
        }

      case req @ POST -> Root / "stream" / UUIDVar(uuid) =>
        for
          streamer <- streamerSvc.get(uuid.toString, Instant.now())
          resp <- streamer match
            case Some(st) => Ok(req.body)
            //case Some(st) => req.body.mapAccumulate(0L) { (count, b) => (count + 1, b) }
            case None => NotFound(())
          //resp <- if streamer.isDefined then Ok("That's a Bingo!") else NotFound("So sorry it didn't work out.")
        yield resp

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

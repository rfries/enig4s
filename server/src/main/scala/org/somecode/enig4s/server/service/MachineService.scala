package org.somecode.enig4s
package server
package service

import cats.effect.Concurrent
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object MachineService:
  def routes[F[_]: Concurrent]: HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl.*
    HttpRoutes.of[F] {
      case GET -> Root / "wheels" =>
        Ok("Ok")
    }
  }

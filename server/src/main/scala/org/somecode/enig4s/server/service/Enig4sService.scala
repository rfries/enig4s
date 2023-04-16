package org.somecode.enig4s
package server
package service

import cats.effect.Async
import cats.implicits.*
import io.circe.Json
import org.http4s.EntityDecoder
import org.http4s.EntityEncoder
import org.http4s.Request
import org.http4s.Response
import org.http4s.dsl.Http4sDsl

trait Enig4sService[F[_]: Async] extends Http4sDsl[F] {

  /**
    * Standard response adapter for code in F that returns an `Either[String, Json]`
    *
    * @param req  The incoming request
    * @param f    The handler to apply
    * @return     The response, ready to hand back to http4s
    */
  def jsonResponseEitherF[A]
      (using EntityDecoder[F, A], EntityEncoder[F, Json])
      (req: Request[F])
      (f: A => F[Either[String, Json]]): F[Response[F]] =

    req.as[A].flatMap(f).flatMap {
      case Left(s) => BadRequest(Json.obj("error" -> Json.fromString(s"Bad Request: $s")))
      case Right(js) => Ok(js)
    }

  /**
    * Standard response adapter for pure code that returns an `Either[String, Json]`
    *
    * @param req  The incoming request
    * @param f    The handler to apply
    * @return     The response, ready to hand back to http4s
    */
  def jsonResponseEither[A]
      (using EntityDecoder[F, A], EntityEncoder[F, Json])
      (req: Request[F])
      (f: A => Either[String, Json]): F[Response[F]] =

    req.as[A].map(f).flatMap {
      case Left(s) => BadRequest(Json.obj("error" -> Json.fromString(s"Bad Request: $s")))
      case Right(js) => Ok(js)
    }
}

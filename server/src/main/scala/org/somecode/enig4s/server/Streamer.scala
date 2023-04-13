package org.somecode.enig4s
package server

import cats.MonadError
import cats.implicits.*
import cats.effect.{Concurrent, Sync}
import cats.effect.kernel.Async
import cats.effect.std.UUIDGen
import org.somecode.enig4s.mach.{Machine, MachineState}

import java.time.Instant
import scala.collection.concurrent.TrieMap
import scala.concurrent.duration.*

final case class Streamer(name: String, machine: Machine, state: MachineState, start: Instant, duration: FiniteDuration):

  private val startMillis: Long = start.toEpochMilli
  private val expiresMillis: Long = startMillis + duration.toMillis

  def expired(now: Instant): Boolean = now.toEpochMilli < expiresMillis
  def valid(now: Instant): Boolean =
    val nowMillis = now.toEpochMilli
    nowMillis >= startMillis && nowMillis < expiresMillis

trait StreamerService[F[_]]:
  def get(name: String, now: Instant): F[Option[Streamer]]
  def create(machine: Machine, state: MachineState, creation: Instant, duration: FiniteDuration): F[Streamer]
  def expire(now: Instant): F[Unit]

object StreamerService:

  def localMapService[F[_]](using F: Sync[F]): F[StreamerService[F]] = F.pure(LocalMapStreamerService[F])
  private class LocalMapStreamerService[F[_]](using F: Sync[F]) extends StreamerService[F]:

    private val streamerMap: collection.concurrent.TrieMap[String, Streamer] = TrieMap.empty

    override def get(name: String, now: Instant): F[Option[Streamer]] = F.pure(streamerMap.get(name).filter(_.valid(now)))

    override def create(machine: Machine, state: MachineState, creation: Instant, duration: FiniteDuration): F[Streamer] =
      for
        uuid <- UUIDGen.randomUUID
        streamer = Streamer(uuid.toString, machine, state, creation, duration)
        _ = streamerMap.put(streamer.name, streamer)
      yield streamer

    override def expire(now: Instant): F[Unit] =
      streamerMap.filterInPlace((_, streamer) => streamer.valid(now))
      F.unit

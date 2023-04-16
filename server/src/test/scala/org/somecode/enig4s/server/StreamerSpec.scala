package org.somecode.enig4s
package server

import cats.effect.unsafe.implicits.*
import cats.effect.implicits.*
import cats.effect.{IO, SyncIO}
import cats.implicits.*
import org.scalatest.EitherValues
import org.somecode.enig4s.mach.*
import org.somecode.enig4s.mach.MachineSpec.*
import org.somecode.enig4s.server.StreamerService

import java.time.Instant
import scala.concurrent.duration.*

class StreamerSpec extends Enig4sSpec with EitherValues:

  import StreamerSpec.*

  "Streamer" should {
    "add an entry that can then be looked up" in {
      val runTest = {
        for
          svc <- StreamerService.localMapService[IO]
          streamer <- svc.create(mach, state, Instant.now(), 5.seconds)
          found <- svc.get(streamer.name, Instant.now())
        yield
          found.isDefined shouldBe true
          found.get should ===(streamer)
      }
      runTest.unsafeRunSync()

    }

    "return only valid entries" in {
      val now = Instant.now()
      val testcase = {
        for
          svc <- StreamerService.localMapService[IO]
          streamer <- svc.create(mach, state, now, 5.seconds)

          tooSoon <- svc.get(streamer.name, now.minusSeconds(3))
          found <- svc.get(streamer.name, now.plusSeconds(1))
          notFound <- svc.get(streamer.name, now.plusSeconds(10))
        yield
          tooSoon.isDefined shouldBe false
          found.isDefined shouldBe true
          found.get should ===(streamer)
          notFound.isDefined shouldBe false
      }
      testcase.unsafeRunSync()
    }

    "expire an entry after the expiration time" in {
      val now = Instant.now()
      val testcase = {
        for
          svc <- StreamerService.localMapService[IO]
          streamer <- svc.create(mach, state, now, 5.seconds)
          found <- svc.get(streamer.name, now.plusSeconds(1))
          _ <- svc.expire(now.plusSeconds(6))
          reallyGone <- svc.get(streamer.name, now.plusSeconds(1))
          notFound <- svc.get(streamer.name, now.plusSeconds(10))
        yield
          found.isDefined shouldBe true
          found.get should ===(streamer)
          reallyGone.isDefined shouldBe false
          notFound.isDefined shouldBe false
      }
      testcase.unsafeRunSync()
    }
  }

object StreamerSpec:

  val mach: Machine = machine(wheels = Vector("I", "II", "III"), reflector = "UKW-B").require
  val state: MachineState = machState(position = "AAA", rings = "AAA").require
  val cabinet: Cabinet = Cabinet().require

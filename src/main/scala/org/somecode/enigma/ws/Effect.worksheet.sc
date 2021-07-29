
import cats.effect.*
import cats.effect.unsafe.implicits.*

val stuff = IO(println("hello ")) >> IO(println("world"))

stuff.unsafeRunSync()



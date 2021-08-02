
import cats.effect.*
import cats.effect.unsafe.implicits.*

val stuff = IO(print("hello ")) >> IO(println("world"))

stuff.unsafeRunSync()


import cats.implicits.*
import org.somecode.enigma.mach.KeyCode

case class PosInt private (n: Int, mod: Int):
  def +(other: Int): PosInt = new PosInt((n + other) % mod, mod)
  def -(other: Int): PosInt = new PosInt((n - other) % mod, mod)

object PosInt:
  def apply(n: Int, mod: Int): Either[String, PosInt] =
    if mod <= 0 then
      Left("mod must be greater than 0")
    else
      Either.cond(n > 0, new PosInt(n, mod), "value must be positivex")

val kc: KeyCode = KeyCode.unsafe(3)
val kc2: KeyCode = KeyCode.unsafe(Char.MaxValue + 3)
def p(n: Int): Unit = println(n)

kc
kc + 1
p(kc)

final case class Alphabet private (size: Int, characterMap: Vector[Char]):
  case class ValidChar private (c: Int)
  def validChars(s: String): Either[String,Vector[ValidChar]] = ???
object Alphabet {}

case class Inter[A](i: Int => A)
trait Monad[M[_]] {
  def flatMap[A, B](a: M[A], f: A => M[B]): M[B]
  def map[A, B](a: M[A], f: A => B): M[B]
}

val InterMonad: Monad[Inter] = new Monad[Inter] {
  def flatMap[A, B](a: Inter[A], f: A => Inter[B]) =
    Inter(n => f(a.i(n)).i(n))
  def map[A, B](a: Inter[A], f: A => B) =
    Inter(n => f(a.i(n)))
}

type StrOrInt = Either[String, Int]

val lstGood: Vector[StrOrInt] = Vector(Right(1), Right(2), Right(3), Right(4), Right(5))
val lstBad1: Vector[StrOrInt] = Vector (Right(1), Right(2), Left("Foo!"), Right(4), Right(5))
val lstBad2: Vector[StrOrInt] = Vector (Right(1), Right(2), Left("Foo!"), Right(4), Left("Bar!"))
val lstBad3: Vector[StrOrInt] = Vector (Right(1), Right(2), Right(3), Right(4), Left("Bar!"))

lstGood.sequence
lstBad1.sequence
lstBad2.sequence
lstBad3.sequence

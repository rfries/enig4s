
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
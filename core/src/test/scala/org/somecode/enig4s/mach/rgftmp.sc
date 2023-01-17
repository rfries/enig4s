import cats.implicits.*

import scala.collection.immutable.Queue

case class Log(s: String):
  def add(n: Int): Log = Log(s + n.toString)

val ns = List(1, 2, 3, 4, 99)

ns.foldLeft((Log(""), Queue.empty[Int])) {
  case ((log, out), n) => (log.add(n), out.enqueue(n))
}

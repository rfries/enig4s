import org.somecode.enig4s.server.api.MachineRequest

import io.circe.{Decoder,HCursor,Json}
import io.circe.parser.*
import cats.implicits.*

val js = """
  {
    "machineType": "M3",
    "wheelNames": [
      "a", "b", "c"
    ],
    "reflectorName": "UKW-B",
    "ringSettings": "PQL",
    "wheelPositions": "AHM",
    "plugs": ["AN", "ST", "ZG", "UH", "KP", "YM"],
    "text": "THIS IS A TEST MESSAGE"
  }
"""

val json = parse(js)

json.flatMap(_.as[MachineRequest]) match {
  case Right(mreq) => println(s">><< $mreq")
  case Left(s) => println(s"!!!! error: $s")
}

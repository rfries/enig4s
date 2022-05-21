import org.somecode.enig4s.mach.Cabinet
import io.circe.*
import io.circe.parser.*
import org.somecode.enig4s.jsapi.MachineRequestJs

val js3 = """ { "n": 1 }"""

val js = """
  {
    "symbolMap": {
      "name": "AZ"
    },
    "wheels": [
      { "name": "I" },
      { "name": "II" },
      { "name": "III" }
    ],
    "reflector": { "name": "UKW-B" },
    "plugboard": {
      "plugs": {
        "symbols": ["AN", "ST", "ZG", "UH", "KP", "YM"]
      }
    },
    "settings": {
      "rings":      { "symbols": "ABC" },
      "wheels":     { "codes": [0, 1, 2] },
      "reflector":  { "symbol": "B" }
    },
    "text": "SENDMORECHUCKBERRY"
  }
"""

val cab = Cabinet.init.getOrElse(throw new RuntimeException("Boo!"))
println("Hello there....")

val json = parse(js)
json match
  case Left(pf) => println("-->" + pf)
  case Right(js) => println("Got JSON: " + js)

json

val mrjs = decode[MachineRequestJs](js)
mrjs match {
  case Left(pf) => println("-->" + pf)
  case Right(mr) =>
    val got = mr.toMachineRequest(cab)
    println("Got JSON: " + got)
    println(mr.plugboard)
    //println("plugboard: " + mr.plugboard)
}


//json.map(j => j.as[MachineRequestJs])

println(">>>> Done!")

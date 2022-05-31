import org.somecode.enig4s.mach.SymbolMap
import io.circe.*
import io.circe.parser.*
import org.scalatest.OptionValues.*
import org.scalatest.EitherValues.*
import org.somecode.enig4s.jsapi.MachineRequestJs
import org.somecode.enig4s.mach.Cabinet

val cab = Cabinet.init.value

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

val json = parse(js).value

val mreqJs = decode[MachineRequestJs](js).value

val mreq = mreqJs.toMachineRequest(cab).value

val (st, tx) = mreq.machine.crypt(mreq.state, mreq.text).value

st.wheelPositions(SymbolMap.AZ)
tx



// json match
//   case Left(pf) => println("-->" + pf)
//   case Right(js) => println("Got JSON: " + js)

// json

// val mrjs = decode[MachineRequestJs](js)
// mrjs match {
//   case Left(pf) => println("-->" + pf)
//   case Right(mr) =>
//     val got = mr.toMachineRequest(cab)
//     println("Got JSON: " + got)
//     println(mr.plugboard)
//     //println("plugboard: " + mr.plugboard)
// }


//json.map(j => j.as[MachineRequestJs])

println(">>>> Done!")

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
      "rings":      { "symbols": "CVG" },
      "wheels":     { "codes": [0, 1, 2] },
      "reflector":  { "symbol": "B" }
    },
    "text": "SENDMORECHUCKBERRY"
  }
"""

val json = parse(js).value
val mreqJs = json.as[MachineRequestJs].value
val mreq = mreqJs.toMachineRequest(cab).value

mreq.state.readable(SymbolMap.AZ)
val (st, text) = mreq.machine.crypt(mreq.state, mreq.text).value

println(st.readable(SymbolMap.AZ))
text

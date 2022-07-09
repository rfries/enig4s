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
      "reflector":  { "symbol": "S" }
    },
    "text": "SENDMORECHUCKBERRY"
  }
"""

val json = parse(js).value
val mreqJs = json.as[MachineRequestJs].value
val mreq = mreqJs.toMachineRequest(cab).value

mreq.state.readable(SymbolMap.AZ)
val csr = mreq.machine.crypt(mreq.state, mreq.text, true).value

println(csr.state.readable(SymbolMap.AZ))
csr.text
csr.traceMsg

val jsonRev = json.hcursor.downField("text").withFocus(_.mapString(_ => csr.text)).top.value

val mreqJsRev = jsonRev.as[MachineRequestJs].value
val mreqRev = mreqJsRev.toMachineRequest(cab).value
mreqRev.state.readable(SymbolMap.AZ)
val csrRev = mreqRev.machine.crypt(mreqRev.state, mreqRev.text, true).value
csrRev.text




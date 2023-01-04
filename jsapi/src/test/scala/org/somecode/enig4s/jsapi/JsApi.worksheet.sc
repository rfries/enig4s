import org.somecode.enig4s.mach.SymbolMap
import _root_.io.circe.*
import _root_.io.circe.parser.*
import org.scalatest.OptionValues.*
import org.scalatest.EitherValues.*
import org.somecode.enig4s.jsapi.MachineRequestJs
import org.somecode.enig4s.mach.Cabinet

val cab = Cabinet().getOrElse(throw new IllegalStateException("Can't init cabinet."))

val js =
  """
    |{
    |  "wheels": [
    |    { "name": "m4.BETA" },
    |    { "name": "V" },
    |    { "name": "VI" },
    |    { "name": "VIII" }
    |  ],
    |  "reflector": { "name": "m4.UKW-C" },
    |  "settings": {
    |    "rings":      { "symbols": "EPEL" },
    |    "positions":  { "symbols": "NAEM" },
    |    "plugboard": {
    |      "plugs": {
    |        "symbols": ["AE", "BF", "CM", "DQ", "HU", "JN", "LX", "PR", "SZ", "VW"]
    |      }
    |    }
    |  },
    |  "text": "QEOB"
    |}
    |
    |""".stripMargin

val json = parse(js).value
val mreqJs = json.as[MachineRequestJs].value
val mreq = mreqJs.toMachineRequest(cab).value

mreq.state.display(SymbolMap.AZ)
val csr = mreq.machine.crypt(mreq.state, mreq.text, true).value

// println(csr.state.display(SymbolMap.AZ))
csr.text
csr.traceMsg

// val jsonRev = json.hcursor.downField("text").withFocus(_.mapString(_ => csr.text)).top.value

// val mreqJsRev = jsonRev.as[MachineRequestJs].value
// val mreqRev = mreqJsRev.toMachineRequest(cab).value
// mreqRev.state.display(SymbolMap.AZ)
// val csrRev = mreqRev.machine.crypt(mreqRev.state, mreqRev.text, true).value
// csrRev.text


// val ll: LazyList[Int] = 0 #:: 1 #:: ll.zip(ll.tail).map((n, m) => n + m)
// ll.take(12).foreach(println)

val js2 = """
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
    "settings": {
      "rings":      { "symbols": "CVG" },
      "wheels":     { "ordinals": [1, 2, 3] },
      "reflector":  { "symbol": "A" },
      "plugboard": {
        "plugs": {
          "symbols": ["AN", "ST", "ZG", "UH", "KP", "YM"]
        }
      }
    },
    "text": "SENDMORECHUCKBERRY"
  }
"""

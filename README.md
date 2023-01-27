# enig4s

Enig4s is an Enigma machine simulator which I wrote while exploring Scala 3.  While I don't think
there is any particular need for another Enigma simulator, I wanted an interesting topic for an exploratory
scala 3 project using cats and http4s, and I've long had a historical interest in Enigma, Bletchley
Park, and technologies related to the early history of computing.

That said, I think it is a decent attempt, if you happen to need a WWII-era Enigma with
a JSON api.  It has pre-defined wirings for most models used during WWII, plus you can define
your own wheels, character set, bus size, etc. within the request if you don't want to use
predefined wirings (this can also simulate a reconfigurable reflector)

Please note that, as far as I am aware, Engima is not useful for encryption in any modern
context, as it is vulnerable to any number of modern code breaking techniques.

Generally, Enig4s simulates a class of rotor-based reciprocal encryption machines which includes Enigma, TypeX, and the
Swiss NEMA machine.  This generalized machine consists of a variable number of rotors, a reflector, and an optional plugboard.

The keyboard and indicator lights (String input and output in the API) are represented by a symbol map that defines
each key and its mapping to a UTF-8 code point. The number of entries in this map corresponds to the number of keys
and indicator lights, and so too to the number of positions in the rotory components and size of all `Wiring`s within
a given machine instance.

The core is purely functional, and contains the machine implementation and a number of pre-defined wirings.

The 'jsapi' module contains the classes and Circe codecs used to translate to and from JSON requests,
and isolates the web server JSON from a direct dependency on the core model.  The 'server' module contains an http4s web
server providing a basic api to the core. All modules except for 'server' are cross-compiled for the JVM and
Scala.JS. The server is JVM only. 

The following example request shows the decryption of an actual naval Enigma message sent to U-538 in May 1945:
```
$ curl -s localhost:8080/enigma -H 'Content-Type: application/json' -d '
{
  "wheels": [
    { "name": "m4.BETA" },
    { "name": "V" },
    { "name": "VI" },
    { "name": "VIII" }
  ],
  "reflector": { "name": "m4.UKW-C" },
  "settings": {
    "positions":  { "symbols": "LWGN" },
    "rings":      { "symbols": "AAEL" },
    "plugboard": {
      "plugs": {
        "symbols": ["AE", "BF", "CM", "DQ", "HU", "JN", "LX", "PR", "SZ", "VW"]
      }
    }
  },
  "text": "SCXBDTZLXVDMEIHBBHNETIJLDFCWBGRMTHSWQNTYOKQGVZKZNJVPGAKDDQAZAGVJHKIFNLQIXOYAK
  FQQUBAKGYAHRDRXLTOVYPNHJDZFTYOCLTIHHCSQBPFTOHDIZJGGDSJICPJDEXIBLDYCMTGYARLTCHJKFNTNLFE
  YGFLYTBILLXTKFXNHPWYYOFLDBQVQ"
}'

```
results in:
```
{
  "text": "VONVONUUUEINSNULNULACHTKKGESSNERKRTXNULNEUNDREINULUHRVONLUEBECKTRAVEMUENDEEINX
  ZUSATZFUERFUNFUUUFLOTTXJTREIBOALVERBRXVOMEINSXVIRXBISDREINULXVIRXSECHSYEINSCBMXBESTANDA
  MEINSXFUNFXXVIRFUNFYNEUNCBM",
  "wheelPositions": "LXVX"
}

```
which translates roughly as
> From U-1008 "(Gessner)": [At] 0938 hours entered [port] at Travemünde from Lübeck. Addition for 5th
> Submarine Flotilla: Fuel oil consumption from 1st April to 30th April 6.1 cubic meters. Remaining [fuel] on 1st May 45.9 cubic meters. "

For details, see [Michael Hörenberg's naval Enigma project](https://enigma.hoerenberg.com/index.php?cat=The%20U534%20messages&page=P1030671)

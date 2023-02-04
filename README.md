# enig4s

Enig4s is an Enigma machine simulator which I wrote for fun while playing
around with Scala 3.

The Enigma machine was an encryption device used by Germany (and several other
countries) prior to and during WWII.
The efforts of Polish and British cryptanalysts in breaking Enigma and other
military ciphers of the era proved crucial both to the
victory of the Allies and in the early development of electro-mechanical and
electronic computing. Ironically, those developments went largely
unacknowledged for decades after the war due to the secrecy imposed by their use.

This seemed like an interesting programming challenge, and I was curious to see
what it would look like in functional scala (and I was just starting to play
around with scala 3 at the time).

Generally, enig4s simulates a class of rotory, reciprocal cipher machines
which includes Enigma, TypeX, and the Swiss NEMA machine. This type of machine
implements a reciprocal substitution cipher, where each character goes through
a series of successive substitutions based on the rotor positions, plugboard
configuration, and other settings.

It can be embedded as a component, and there is a module that exposes a [JSON API](doc/JsApi.md).
There are pre-defined wirings for most models used during WWII, plus you can
define your own wheels, character set, bus size, etc. (this can also simulate
a reconfigurable reflector).

Please note that, as far as I am aware, Engima is not useful for
encryption in a modern context, as it is vulnerable to any number
of modern code breaking techniques (and indeed it was, as it turns out,
vulnerable at the time).

### Structure

The `core` is purely functional, and contains the machine implementation and
a number of pre-defined wirings.

The `jsapi` module contains the classes and Circe codecs used to translate
to and from JSON requests, and isolates the web server JSON from the core model.

The `server` module contains an `http4s` server providing a JSON api.

All modules
except for 'server' are cross-compiled for the JVM and Scala.JS. The server is JVM only. 

### Example

The following example request shows the decryption of a naval Enigma
message received by U-538 in May 1945:
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
which has been interpreted as
> From U-1008 "(Gessner)": [At] 0938 hours entered [port] at Travemünde from Lübeck. Addition for 5th
> Submarine Flotilla: Fuel oil consumption from 1st April to 30th April 6.1 cubic meters. Remaining [fuel] on 1st May 45.9 cubic meters. "

For details, see [Michael Hörenberg's naval Enigma project](https://enigma.hoerenberg.com/index.php?cat=The%20U534%20messages&page=P1030671).

### Planned Additions

These items are still in progress:

- Support for Enigma K stepping (gear/cog drive)
- Support for TypeX stepping (with static wheels) and TypeX style plugboard.

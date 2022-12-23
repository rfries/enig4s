# enig4s

Enig4s is an Enigma machine simulator which I wrote while exploring Scala 3.  While I don't think
there is any particular need for another Enigma simulator, I wanted an interesting topic for an exploratory
scala 3 project using cats and http4s, and I've long had a historical interest in Enigma, Bletchley
Park, and technologies related to the early history of computing.

That said, I think it is a decent attempt, if you happen to need a WWII-era Enigma with
a JSON api.  It has pre-defined wirings for most models used during WWII, plus you can define
your own wheels, character set, bus size, etc. within the request if you don't want to use
predefined wirings (this can also simulate a reconfigurable reflector or TypeX style plug board)

Please note that, as far as I am aware, Engima is not useful for encryption in any modern
context, as it is vulnerable to any number of modern code breaking techniques.

Generally, Enig4s simulates a class of rotor-based reciprocal encryption machines which includes Enigma, TypeX, and the
Swiss NEMA machine.  This generalized machine consists of a variable number of rotors, a reflector, and an optional plugboard.

The keyboard and indicator lights (String input and output in the API) are represented by a symbol map that defines
each key and its mapping to a UTF-8 code point. The number of entries in this map corresponds to the number of keys
and indicator lights, and so too to the number of positions in the rotory components and size of all `Wiring`s within
a given machine instance.

The core is purely functional, and has no dependencies other than cats (because traverse), and scalatest for testing.
At least that was the intention, right now it also depends on typelevel's `CIString` for case-insensitive map keys,
because I have been too lazy to move the `Cabinet` (a library of pre-defined wirings) out of the core.

The 'jsapi' module contains the classes and Circe codecs used to translate to and from JSON requests,
and isolates the web server JSON from a direct dependency on the core model.  The 'server' module contains an http4s web
server providing a basic api to the core. All modules except for 'server' are cross-compiled for the JVM and
Scala.JS. The server is JVM only. 

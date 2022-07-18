# enig4s

Enig4s is an Enigma machine simulator written in functional Scala.  I wrote it because I wanted to play around with Scala 3 and
http4s, and I always had an interest in Enigma, Lorenz (Tunny), and other encryption and codebreaking technologies related
to the early history of computing.

Generally, it simulates a class of rotor-based reciprocal encryption machines which includes Enigma and the British TypeX.

## Concepts

The machine, as well as various internal components, perform transformations on values known as KeyCodes, which represent the codes
produced by the keyboard and displayed by the indicator lights.  KeyCodes are are positive integers from 0 through n-1, where n is
the bus size of the machine (26 for most versions of the Enigma).

The translation of symbols to key codes is done by a `SymbolMap`,
which can be specified on Machine construction, which maps a String of unicode symbols to numeric key codes.  The default SymbolMap, "AZ", maps the characters A-Z (i.e. Unicde/ASCII 'A' through 'Z')to the key codes 0-25.

Internally, a Machine consists of several components, most of which transform key codes in various ways.

### Wiring

`Wiring` is a bus component that represents a static mapping of values, such as the keyboard entry plate (`Keyboard`), a fixed `Reflector`,
or the core of a Rotor.

### Rotor (trait)

A `Rotor` is a stateful bus component which contains a static Wiring (representing the core), but which also utilizes the current
rotor state (position and ring setting) to calculate the transformation.

### Plugboard

A `PlugBoard` is a stateful component that allows for the swapping of specified values in pairs,
for example the pair "AN" specifies that all A's become 'N's, and that all 'N's become 'A's. All
values not specified in the plug pairs are passed through unchanged.

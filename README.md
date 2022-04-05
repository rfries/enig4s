# enig4s
Implementation of a World War II era Enigma machine written in reasonably functional Scala.

## Concepts

The machine, as well as various internal components, perform transformations on values known as KeyCodes, which represent the codes produced by the keyboard and displayed by the indicator lights.
KeyCodes are are positive integers from 0 through n-1, where n is the bus size of the machine (26 for most versions of the Enigma). The translation of symbols to key codes is done by a SymbolMap, which can be specified on Machine construction, which maps a String of unicode symbols to numeric key codes.  The default SymbolMap, "AZ", maps the characters A-Z (i.e. Unicde/ASCII 'A' through 'Z')to the key codes 0-25.

Internally, a Machine consists of several components, most of which transform key codes in various
ways.

### Wiring

`Wiring` is a bus component that represents a static mapping of values, such as the keyboard entry plate (`Keyboard`) or a fixed `Reflector`.

### Rotor (trait)

The Rotor is a stateful bus component which utilizes a Wiring (representing the core), but which also utilizes machine state (rotor position and ring setting) to add offsets to the translation using modulo n arithmetic (where n is the bus size).

### Plugboard

A `PlugBoard` is a stateful component that allows for the swapping of specified values in pairs,
for example the pair "AN" specifies that all A's become 'N's, and that all 'N's become 'A's. All
other values are passed through unchanged.

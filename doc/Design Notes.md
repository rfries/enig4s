## Concepts

The machine is represented by an immutable Machine instance, which the defines the number wheels, bus size, wheel and reflector
wiring, keyboard/symbol mapping, and plug-board settings.  The machine state, which is configured per-message, contains the wheel positions, ring settings, and plugboard configuration.

The keyboard is represented
by a `SymbolMap`, which defines all possible keys (and so the bus size) and their mapping to UTF-8 code points.  Internally these keys
are represented by integers from 0 to n-1, where n is the bus size for the machine; all translation to and from the internal representation
is done via a `SymbolMap`. The default SymbolMap, "AZ", maps the characters A-Z (i.e. Unicde/ASCII 'A' through 'Z')to the key codes 0-25.

Internally, a Machine consists of several components, most of which transform key codes in various ways.

### Wiring

Most components contain one or more `Wiring`s, which performs a static lookup translation on an
incoming value.  These are used to represent the wiring of the wheel cores, entry disk, and
reflector.

### Entry Disc

This is a components that consist of a single static wiring instance, and represents the connection
between the keyboard and the rotors.  In most Enigma models, the wiring was straight through, and performed no translation.

### Wheel

A `Wheel` is a stateful bus component which contains a static Wiring (representing the core), but which also utilizes the current rotor state (position and ring setting) to calculate the transformation. Wheels perform transformations twice during the processing of a single character, both in the forward direction and in the reflected return.

### Reflector

This is a variation on a wheel that represents the Enigma reflector, which uses its wiring to
"reflect" its input in order to begin the return path through the wheels.  Reflectors have
the additional restriction that no letter can map (be wired) to itself, since that would
prevent a return path.  In some models, the reflector can be set to different positions,
but there is no equivalent of a ring setting.

### Plugboard

A `PlugBoard` is a stateful component that allows for the swapping of specified values in pairs,
for example the pair "AN" specifies that all A's become 'N's, and that all 'N's become 'A's. All
values not specified in the plug pairs are passed through unchanged.

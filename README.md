# enig4s
Functional scala implementation of an enigma machine.

 ## Concepts

### Bus (trait)

The `Bus` trait indicates a sized component that provides forward and reverse mapping
between two integer values from 0 (inclusive) to _size_ (exclusive). `Wiring`s, `Wheel`s,
`Reflector`s, `Plugboard`s, and the keyboard entry plate are examples of `Bus` components.

### Wiring

A `Wiring` is a bus component that represents a static mapping of values, without rotation or
ring settings, such as the keyboard entry plate (`Keyboard`) or a fixed reflector.

### Rotor (trait)

The Rotor trait indicates a stateful bus component which, in addition to a `Wiring`,
uses a rotor position when transforming values.

### Wheel

A Wheel component is a Rotor which adds a ring setting offset when transforming values,
and a set of notch positions which can trigger an adjacent rotor to advance.

### Plugboard

A `Plugboard` is a `Bus` component that allows for the swapping of specified values in pairs,
for example the pair "AN" specifies that all A's become 'N's, and that all 'N's become 'A's. All
other values are passed through unchanged.

# enig4s
Functional scala implementation of an enigma machine.

 ## Concepts

### Bus (trait)

The `Bus` trait indicates a sized component that provides forward and reverse mapping
between two integer values from 0 (inclusive) to _size_ (exclusive). `Wiring`s, `Wheel`s,
`Reflector`s, `Plugboard`s, and the keyboard entry plate are examples of a `Bus`.

### Wiring

A `Wiring` is a bus component that represents a static, stateless mapping of values,
such as the keyboard entry plate.

### Rotor (trait)

The Rotor trait indicates a stateful component which, in addition to a `Wiring`, uses a rotor position when transforming values.

### Wheel

A Wheel component is a Rotor which adds a ring setting offset when transforming values,
in addition to a set of notch positions which can trigger an adjacent rotor to advance.

### Plugboard

A `Plugboard` is a `Bus` component that allows for the swapping of values
in pairs

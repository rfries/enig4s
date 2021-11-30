# enig4s
Functional scala implementation of an enigma machine.

 ## Concepts

### Bus

The `Bus` trait indicates a sized component that provides forward and reverse mapping
between two integer values from 0 (inclusive) to _size_ (exclusive). `Wiring`s, `Wheel`s,
`Reflector`s, `Plugboard`s, and the keyboard entry plate are examples of a `Bus`.

### Wiring

A `Wiring` is a bus component that represents a static, stateless mapping of values,
such as the keyboard entry plate.

### Rotor
The Rotor trait indicates a stateful mapping of values, with translation
and reverse translation requiring a rotor position for each value.
### Wheel
A Wheel component is a Rotor which uses a ring setting and rotor position
for translation and reverse translation.

### Plugboard
A `Plugboard` is a `Bus` component that allows for the swapping of values
in pairs

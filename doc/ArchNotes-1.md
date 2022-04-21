
```
KeyCode => KeyCode                          // Wiring
KeyCode, Position => KeyCode                // Reflector
KeyCode, Position, RingSetting => KeyCode   // Wheel
```
Wiring:     (no state)
Keyboard:   (no state)
Reflector:  Position (sometimes)
Wheel:      Position, RingSetting

M3: 3 wheel, ratchet, fixed reflector, plugboard
M4: M3 + 4th wheel (4th doesn't move, position 'A' is M3 compatible)
Swiss K: 3 wheel + fixed reflector(26)
D: 3 wheel + fixed reflector(26)
C: 3 wheel + reflector(2)
G: 3 wheel + moving reflector(26)
Zählwerk: 3 wheel + moving reflector(26)


Enigma Ratchet: fixed reflector (multi-position), ratchet/double step, fixed high wheel(s)
Enigma Gear/Counter: moving reflector
TypeX: fixed low wheels, moving reflector

Stepping:
  Enigma:     ratchet/pawl, 0-n
  Swiss K:    rightmost stationary, middle always steps, left advances at notch, double step?
  Enigma G:   gear/cog, stepping includes reflector

## Design Notes

Static: (?)

  - Cores
  - (Cores + Notches) => Wheel
  - (Cores + Allowed Positions) => Reflector
  - Plugboard (w/ or w/o plugs)
  - (Wheels + Reflector + Plugboard) => Machine

### Configuration (static during session):

  - Ring Settings
  - Plugboard Settings
  - Initial Positions

### Changing in Session:

  - Wheel & Reflector Positions

### Changing during a single transform:

- Trace log => Queue[String]
- No difference between wiring, ring settings, plug settings, wheel selection/order, they are all configuration.
- All configuration provided on construction
- Cabinet contains configurations only

### Machine Creation

- Entry Disk (default straighthrough?)
- Wheels
- Reflector
- Maybe Plugboard 
- Symbol Map (default AZ?)

- Compact representation? (I/II/III|RIN)




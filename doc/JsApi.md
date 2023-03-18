
## Endpoints
The web API supports the following endpoints:

|URL                |Method |Description                  |
|-------------------|-------|-----------------------------|
|/enigma            |POST   |Encypt/Decrypt request       |
|/enigma/reflectors |GET    |List pre-defined reflectors  |
|/enigma/symbolmaps |GET    |List pre-defined symbol maps |
|/enigma/wheels     |GET    |List pre-defined wheels      |
|/enigma/wirings    |GET    |List pre-defined wirings     |

### POST /enigma (Encypt/Decrypt)

There are three basic parts to each Machine Request:

- machine definition
- machine settings
- text to transform

The machine definitions define the structural parts of the machine, such as the symbol map,
wheel and reflector wiring, wheel order, and the like.

The machine settings represent the per-message settings, such as the starting wheel positions, ring settings, and plugboard configuration.

In general, components with wiring can be specified by either referencing a pre-defined component
via the name attribute, or inline by specifying a `wiring` object.

The following is an example of a machine request, with all attributes defined (Note that
some of these attributes are mutually exclusive, and others have useful defaults, so only a subset
of these attributes would typically be used)

```json
  {
    "symbolMap": {
      "name": "AZ",
      "mapping": {
        "symbols": "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
        "codepoints": [65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90]
      }
    },
    "keyboard": {
      "name": "AZ",
      "wiring": {
        "symbols": "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
        "numbers": [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26]
      }
    },
    "wheels": [
      {
        "name": "IV",
        "wiring": {
          "symbols": "ESOVPZJAYQUIRHXLNFTGKDCMWB",
          "numbers": [5,19,15,22,16,26,10,1,25,17,21,9,18,8,24,12,14,6,20,7,11,4,3,13,23,2]
        },
        "notches": { "symbols": "NR", "numbers": [14, 18] }
      }
    ],
    "reflector": {
      "name": "UKW-B",
      "wiring": {
        "symbols": "YRUHQSLDPXNGOKMIEBFZCWVJAT",
        "numbers": [6,22,16,10,9,1,15,25,5,4,18,26,24,23,7,3,20,11,21,17,19,2,14,13,8,12]
      }
    },
    "settings": {
      "rings":      { "symbols": "ABC", "numbers": [1, 2, 3] },
      "positions":  { "symbols": "QEL", "numbers": [17, 5, 12] },
      "reflector":  { "symbol": "B", "number": 2 },
      "plugs": {
        "symbols": ["AN", "ST", "ZG", "UH", "KP", "YM"],
        "numbers": [[1, 14], [19, 20], [26, 7], [21, 8], [11, 16], [25, 13]]
      },
      "trace": false
    },
    "text": "SENDMORECHUCKBERRY"
  }
```

### Top Level Attributes

| Attribute | Optional | Default | Description                                                |
|-----------|----------|---------|------------------------------------------------------------|
| symbolMap | optional | "A-Z"   | inline character map definition                            |
| keyboard  | optional | "A-Z"   | inline keyboard mapping                                    |
| wheels    | required | n/a     | wheel order and inline definitions (if any)                |
| reflector | required | n/a     | reflector name or definition                               |
| settings  | required | n/a     | wheel positions, ring settings, and plugboard configuration |
| text      | required | n/a     | input text                                                 |

Other API validation rules:
- All 'symbols' strings must be a subset of the symbol map in use.
- For wiring specifications, only one of "symbols", "numbers", or "indices" should be present.

### Named Components

In general, components such as the wheels and reflector can be specified in full, by
providing a wiring (and notches, for a wheel), or by name, referencing a pre-defined wiring.

A list of available component names can be found [here](Cabinet.md).


### Symbol Maps

The symbol map defines the character range of the machine instance as a whole
as well as the mapping of individual unicode code points to each position or input
in the wheels, keyboard, reflector, and other components in the machine. The default symbol map, "AZ", represents the 26 letters A-Z (the most common character set in actual Enigma machine).

### Wiring

The wiring of wheels, reflectors, and other components is specified by providing either a string
or an array of numbers. When using a string, the "symbols" attribute is used, and in the provided
string each character position represents a mapping to the character specified at that position.
For instance, assuming for brevity a 4-position wheel with a symbol map of "ABCD", then the attribute
```"symbols": "BDAC"``` would indicate mappings of A -> B, B -> D, C -> A, and D -> C.

If specified using (ordinal)
numbers, then:
```"numbers": [2,4,1,3]```
represents the same mapping. The ordinal numbers correspond to the numeric labels used on wheels,
reflectors, and plugboards of some Enigma models.

In addition, 0-based indices can be used by specifying the "indices"
attribute, so:
```"indices": [1,3,0,2]```
describes the same wiring as the previous two examples. 

# MachineRequest

There are three basic parts to each MachineRequest:

- machine definition
- machine settings
- text to transform

The machine definition represents the structural parts of the machine, such as the symbol map, the wheel and reflector wiring, wheel order, and the like.

The machine settings represent the configuration for the parts of the defined machine,
such as the starting wheel positions, ring settings, and plugboard configuration.

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
        "name": "I",
        "wiring": {
          "symbols": "BCDEFGHIJKLMNOPQRSTUVWXYZA",
          "numbers": [2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,1]
        },
        "notches": { "symbols": "NR", "numbers": [1, 16] }
      }
    ],
    "reflector": {
      "name": "UKW-B",
      "wiring": {
        "symbols": "EJMZALYXVBWFCRQUONTSPIKHGD",
        "numbers": [4,9,12,25,0,11,24,23,21,1,22,5,2,17,16,20,14,13,19,18,15,8,10,7,6,3]
      },
      "positions": {
        "symbols": "EJMZALYXVBWFCRQUONTSPIKHGD",
        "numbers": [4,9,12,25,0,11,24,23,21,1,22,5,2,17,16,20,14,13,19,18,15,8,10,7,6,3]
      }
    },
    "settings": {
      "rings":      { "symbols": "ABC", "numbers": [0, 1, 2] },
      "wheels":     { "symbols": "ABC", "numbers": [0, 1, 2] },
      "reflector":  { "symbol": "B", "number": 2 },
      "plugs": {
        "symbols": ["AN", "ST", "ZG", "UH", "KP", "YM"],
        "numbers": [[0, 13], [18, 19], [25, 6], [20, 7], [10, 15], [24, 12]]
      }
    },
    "text": "SENDMORECHUCKBERRY"
  }
```

### Named Components

In general, components such as the wheels and reflector can be specified in full, by
providing a wiring (and notches, for a wheel), or by name, referencing a pre-defined wiring.  
A list of available component names can be found [here](Cabinet.md).


### Wiring

The wiring of wheels, reflectors, and other components is specified by providing either a string
or an array of numbers. When using a string, the "symbols" attribute is used, and in the provided
string each character position represents a mapping to the character specified at that position.
For instance, assuming for brevity a 4-position wheel with a symbol map of "ABCD", then the attribute
```"symbols": "BDAC"```
would indicate that the first position of the wheel ("A" in the symbol map) is mapped to "B",
the second position ("B" in the symbol map) is mapped to "D", and so on.  If specified using (ordinal)
numbers, then:
```"numbers": [2,4,1,3]```
represents the same mapping. In addition, 0-based indices can be used by specifying the "indices"
attribute, so:
```"indices": [1,3,0,2]```
describes the same wiring as the previous two examples. 

The 1-based (ordinal) numbers correspond to the numeric labels used on wheels, reflectors, and
plugboards of some Enigma models.

### Top Level Attributes

| Attribute | Optional | Default | Description                                                |
|-----------|----------|---------|------------------------------------------------------------|
| symbolMap | optional | "A-Z"   | inline character map definition                            |
| keyboard  | optional | "A-Z"   | inline keyboard mapping                                    |
| wheels    | required | n/a     | wheel definitions                                          |
| reflector | required | n/a     | reflector mapping                                          |
| settings  | required | n/a     | wheel positions and ring settings, plugboard configuration |
| text      | required | n/a     | input text                                                 |

Other API validation rules:
- All 'symbols' strings must be a subset of the symbol map in use.
- For wiring specifications, only one of "symbols", "numbers", or "indices" should be present.

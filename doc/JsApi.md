# MachineRequest

There are three basic parts to each MachineRequest:

- machine definition
- machine settings
- text to transform

The machine definition represents the structural parts of the machine, such as number of wheels, bus size (number of characters), presence of plugboard, and the like.

The machine settings represent the configuration for the parts of the defined machine,
such as wheel wiring and order, ring settings, plugboard configuration, and the like.
These are roughly analagous the "user configurable" settings of an actual machine, which
do not change during symbol translation.

The wheel state represents the subset of machine state which changes during each symbol translation.  As such, any successful symbol translation will result in a new wheel state.


```json
  {
    "version": "0.0.1",
    "machine": {
      "name": "M3",
      "drive": "ratchet",         // ratchet (default) | gear
      "plugboard": "enigma",   // none (default) | enigma | typex
      "symbolMap": {
        "name": "AZ",
        "mapping": {
          "symbols": "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
          "codes": [65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90]
        }
      },
      "keyboard": {
        "name": "AZ",
        "wiring": {
          "symbols": "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
          "codes": [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25]
        }
      },
      "reflector": {
        "name": "UKW-B",
        "wiring": {
          "symbols": "EJMZALYXVBWFCRQUONTSPIKHGD",
          "codes": [4,9,12,25,0,11,24,23,21,1,22,5,2,17,16,20,14,13,19,18,15,8,10,7,6,3]
        },
        "positions": {
          "symbols": "EJMZALYXVBWFCRQUONTSPIKHGD",
          "codes": [4,9,12,25,0,11,24,23,21,1,22,5,2,17,16,20,14,13,19,18,15,8,10,7,6,3]
        },
        "advance": false
      },
      "settings": {
        "rings":      { "symbols": "ABC", "codes": [0, 1, 2] },
        "wheels": [
          {
            "name": "I",
            "wiring": {
              "symbols": "YZABCDEFGHIJKLMNOPQRSTUVWX",
              "codes": [2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,0,1]
            },
            "notches": { "symbols": "NR", "codes": [1, 16] }
          }
        ],
        "reflector":  { "symbol": "B", "code": 2 },
        "plugs": {
          "symbols": ["AN", "ST", "ZG", "UH", "KP", "YM"],
          "codes": [[0, 13], [18, 19], [25, 6], [20, 7], [10, 15], [24, 12]]
        }
      }
    },
    "wheelSettings": { "symbols": "ABC","codes": [0, 1, 2] },
    "text": "SENDMORECHUCKBERRY"
  }
```

```json
  {
    "drive": "ratchet", // ratchet or gear
    "symbolMap": {
      "name": "AZ",
      "mapping": {
        "symbols": "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
        "codes": [65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90]
      }
    },
    "keyboard": {
      "name": "AZ",
      "wiring": {
        "symbols": "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
        "codes": [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25]
      }
    },
    "wheels": [
      {
        "name": "I",
        "wiring": {
          "symbols": "YZABCDEFGHIJKLMNOPQRSTUVWX",
          "codes": [2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,0,1]
        },
        "notches": { "symbols": "NR", "codes": [1, 16] }
      }
    ],
    "reflector": {
      "name": "UKW-B",
      "wiring": {
        "symbols": "EJMZALYXVBWFCRQUONTSPIKHGD",
        "codes": [4,9,12,25,0,11,24,23,21,1,22,5,2,17,16,20,14,13,19,18,15,8,10,7,6,3]
      },
      "positions": {
        "symbols": "EJMZALYXVBWFCRQUONTSPIKHGD",
        "codes": [4,9,12,25,0,11,24,23,21,1,22,5,2,17,16,20,14,13,19,18,15,8,10,7,6,3]
      },
      "advance": false
    },
    "settings": {
      "rings":      { "symbols": "ABC", "codes": [0, 1, 2] },
      "wheels":     { "symbols": "ABC", "codes": [0, 1, 2] },
      "reflector":  { "symbol": "B", "code": 2 },
      "plugs": {
        "symbols": ["AN", "ST", "ZG", "UH", "KP", "YM"],
        "codes": [[0, 13], [18, 19], [25, 6], [20, 7], [10, 15], [24, 12]]
      }
    },
    "text": "SENDMORECHUCKBERRY"
  }
```



```json
  {
    "drive": "ratchet", // ratchet or gear
    "symbolMap": {
      "name": "AZ",
      "mapping": {
        "symbols": "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
        "codes": [65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90]
      }
    },
    "keyboard": {
      "name": "AZ",
      "wiring": {
        "symbols": "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
        "codes": [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25]
      }
    },
    "wheels": [
      {
        "name": "I",
        "wiring": {
          "symbols": "YZABCDEFGHIJKLMNOPQRSTUVWX",
          "codes": [2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,0,1]
        },
        "notches": { "symbols": "NR", "codes": [1, 16] }
      }
    ],
    "reflector": {
      "name": "UKW-B",
      "wiring": {
        "symbols": "EJMZALYXVBWFCRQUONTSPIKHGD",
        "codes": [4,9,12,25,0,11,24,23,21,1,22,5,2,17,16,20,14,13,19,18,15,8,10,7,6,3]
      },
      "positions": {
        "symbols": "EJMZALYXVBWFCRQUONTSPIKHGD",
        "codes": [4,9,12,25,0,11,24,23,21,1,22,5,2,17,16,20,14,13,19,18,15,8,10,7,6,3]
      },
      "advance": false
    },
    "settings": {
      "rings":      { "symbols": "ABC", "codes": [0, 1, 2] },
      "wheels":     { "symbols": "ABC", "codes": [0, 1, 2] },
      "reflector":  { "symbol": "B", "code": 2 },
      "plugs": {
        "symbols": ["AN", "ST", "ZG", "UH", "KP", "YM"],
        "codes": [[0, 13], [18, 19], [25, 6], [20, 7], [10, 15], [24, 12]]
      }
    },
    "text": "SENDMORECHUCKBERRY"
  }
```


```json
  {
    "busSize": 26,
    "characterMap": {
      "name": "AZ",
      "mapping": "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
      "codes": [65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90]
    },
    "keyboard": {
      "name": "AZ",
      "mapping": "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
      "codes": [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25]
    },
    "wheels": [
      {
        "name": "I",
        "mapping": "YZABCDEFGHIJKLMNOPQRSTUVWX",
        "codes": [2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,0,1],
        "notches": "NR",
        "notchCodes": [1, 16]
      }
    ],
    "reflector": {
      "name": "UKW-B",
      "mapping": "EJMZALYXVBWFCRQUONTSPIKHGD",
      "codes": [4,9,12,25,0,11,24,23,21,1,22,5,2,17,16,20,14,13,19,18,15,8,10,7,6,3]
    },
    "settings": {
      "rings": { "mapping": "ABC", "codes": [0, 1, 2] },
      "wheels": { "mapping": "ABC", "codes": [0, 1, 2] },
      "reflector": { "mapping": "B", "codes": [2] },
      "plugs": {
        "mapping": ["AN", "ST", "ZG", "UH", "KP", "YM"],
        "codes": [[0, 13], [18, 19], [25, 6], [20, 7], [10, 15], [24, 12]]
      }
    },
    "text": "SENDMORECHUCKBERRY"
  }
```


```json
  {
    "busSize": 26,
    "characterMap": {
      "name": "AZ",
      "mapping": "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
      "codes": [65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90]
    },
    "keyboard": {
      "name": "AZ",
      "mapping": "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
      "codes": [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25]
    },
    "wheels": [
      {
        "name": "I",
        "mapping": "YZABCDEFGHIJKLMNOPQRSTUVWX",
        "codes": [2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,0,1],
        "notches": { "mapping": "NR", "codes": [1, 16] },
        "position": { "mapping": "A", "code": 1 },
        "ringSetting": { "mapping": "A", "code": 1 }
      }
    ],
    "reflector": {
      "name": "UKW-B",
      "mapping": "EJMZALYXVBWFCRQUONTSPIKHGD",
      "codes": [4,9,12,25,0,11,24,23,21,1,22,5,2,17,16,20,14,13,19,18,15,8,10,7,6,3],
      "position": { "mapping": "A", "code": 1 }
    },
    "plugs": {
      "mapping": ["AN", "ST", "ZG", "UH", "KP", "YM"],
      "codes": [[0, 13], [18, 19], [25, 6], [20, 7], [10, 15], [24, 12]]
    },
    "text": "SENDMORECHUCKBERRY"
  }
```
## Attributes

| Attribute        | Optional  | Default | Description                                                    |
|------------------|-----------|---------|----------------------------------------------------------------|
| busSize          | optional  | 26      | size of all bus components                                     |
| characterMapName | optional  | "AZ"    | name of predefined character map                               |
| characterMap     | optional  | n/a     | inline character map definition                                |
| keyboardName     | optional  | "AZ"    | name of predefined keyboard                                    |
| keyboard         | optional  | n/a     | inline keyboard mapping                                        |
| wheels           | required  | n/a     | wheel definitions                                              |
| reflector        | required  | n/a     | reflector mapping                                              |
| plugs            | required  | n/a     | plugboard pairs                                                |
| text             | optional  | n/a     | input text, if present -- if absent, input is the request body |

Other API validation rules:
- Only one of `characterMapName` or `characterMap` may exist.
- Only one of `keyboardName` or `keyboard` may exist.
- Either `reflectorName` or `reflector` (but not both) must exist.
- In mapping objects, either `mapping` or `codes` (but not both) must exist.
- In wheel objects, only one of ``
- All strings must be a subset of the character map in use.


```json
  {
    "busSize": 26,
    "characterMap": {
      "name": "AZ",
      "mapping": "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
      "codes": [65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90]
    },
    "keyboard": {
      "name": "AZ",
      "mapping": "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
      "codes": [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25]
    },
    "wheels": [
      {
        "name": "I",
        "mapping": "YZABCDEFGHIJKLMNOPQRSTUVWX",
        "codes": [2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,0,1],
        "notches": { "mapping": "NR", "codes": [1, 16] }
      }
    ],
    "reflector": {
      "name": "UKW-B",
      "mapping": "EJMZALYXVBWFCRQUONTSPIKHGD",
      "codes": [4,9,12,25,0,11,24,23,21,1,22,5,2,17,16,20,14,13,19,18,15,8,10,7,6,3]
    },
    "plugs": {
      "mapping": ["AN", "ST", "ZG", "UH", "KP", "YM"],
      "codes": [[0, 13], [18, 19], [25, 6], [20, 7], [10, 15], [24, 12]]
    },
    "state": {
      "wheelPositions": "ABC",
      "wheelPositionCodes": [1, 2, 3],
      "ringSettings": "ABC",
      "ringSettingCodes": [1, 2, 3],
      "reflectorPosition": "A",
      "reflectorPositionCode": 0
    },
    "text": "SENDMORECHUCKBERRY"
  }
```


```json
  {
    "state": {
      "wheelPositions": "ABC",
      "wheelPositionCodes": [1, 2, 3],
      "ringSettings": "ABC",
      "ringSettingCodes": [1, 2, 3],
      "reflectorPosition": "A",
      "reflectorPositionCode": 0
    },
    "text": "SENDMORECHUCKBERRY"
  }
```


## Attributes

| Attribute        | Optional  | Default | Description                                                    |
|------------------|-----------|---------|----------------------------------------------------------------|
| busSize          | optional  | 26      | size of all bus components                                     |
| characterMapName | optional  | "AZ"    | name of predefined character map                               |
| characterMap     | optional  | n/a     | inline character map definition                                |
| keyboardName     | optional  | "AZ"    | name of predefined keyboard                                    |
| keyboard         | optional  | n/a     | inline keyboard mapping                                        |
| wheels           | required  | n/a     | wheel definitions                                              |
| reflector        | required  | n/a     | reflector mapping                                              |
| plugs            | required  | n/a     | plugboard pairs                                                |
| text             | optional  | n/a     | input text, if present -- if absent, input is the request body |

Other API validation rules:
- Only one of `characterMapName` or `characterMap` may exist.
- Only one of `keyboardName` or `keyboard` may exist.
- Either `reflectorName` or `reflector` (but not both) must exist.
- In mapping objects, either `mapping` or `codes` (but not both) must exist.
- In wheel objects, only one of ``
- All strings must be a subset of the character map in use.

## Not as Old Format
```json
    {
      "busSize": 26,
      "characterMapName": "AZ",
      "characterMap": {
        "mapping": "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
        "codes": [65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90]
      },
      "keyboardName": "AZ",
      "keyboard": {
        "mapping": "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
        "codes": [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25]
      },
      "wheels": [
        {
          "wheelName": "I",
          "mapping": "YZABCDEFGHIJKLMNOPQRSTUVWX",
          "codes": [2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,0,1],

          "position": "A",
          "ringSetting": "P"
        }
      ],
      "reflectorName": "UKW-B",
      "reflector": {
        "mapping": "EJMZALYXVBWFCRQUONTSPIKHGD",
        "codes": [4,9,12,25,0,11,24,23,21,1,22,5,2,17,16,20,14,13,19,18,15,8,10,7,6,3]
      },
      "plugs": {
        "mapping": ["AN", "ST", "ZG", "UH", "KP", "YM"],
        "codes": [[0, 13], [18, 19], [25, 6], [20, 7], [10, 15], [24, 12]]
      },
      "state": {
        "wheels": [
          {
            "ringSetting": 0,
            "position": 5
          }
        ],
        "reflectorPosition": 0
      },
      "text": "SENDMORECHUCKBERRY"
    }
  ```

## Old Format

  ```json
    {
      "busSize": 26,
      "charMap": "AZ",
      "keyboardCodes": [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25],
      "wheels": [
        {
          "wheelName": "I",
          "position": "A",
          "ringSetting": "P"
        },
        {
          "wheelName": "II",
          "position": "Q",
          "ringSetting": "E"
        },
        {
          "mapping": "YZABCDEFGHIJKLMNOPQRSTUVWX",
          "notches": "CJ",
          "position": "M",
          "ringSetting": "C"
        },
        {
          "codes": [2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,0,1],
          "notches": [3, 14],
          "position": "M",

          "ringSetting": "C"
        }
      ],
      "reflectorName": "UKW-B",
      "plugs": ["AN", "ST", "ZG", "UH", "KP", "YM"],
      "text": "THIS IS A TEST MESSAGE"
    }

```

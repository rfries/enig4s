# MachineRequest

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
      "wheelPositions": "ABC",
      "ringSettings": "ABC",
      "reflectorPosition": 0
    },
    "text": "SENDMORECHUCKBERRY"
  }
```

## Attributes

| Attribute        | Optional  | Default | Description                                                     |
|------------------|-----------|---------|-----------------------------------------------------------------|
| busSize          | optional  | 26      | size of all bus components                                      |
| characterMapName | optional  | "AZ"    | name of predefined character map                                |
| characterMap     | optional  | n/a     | inline character map definition                                 |
| keyboardName     | optional  | "AZ"    | name of predefined keyboard                                     |
| keyboard         | optional  | n/a     | inline keyboard mapping                                         |
| wheels           | required  | n/a     | wheel definitions                                               |
| reflectorName    | required* | n/a     | name of predefined reflector (required if not defined inline)   |
| reflector        | required  | n/a     | inline reflector mapping                                        |
| plugs            | required  | n/a     | plugboard pairs                                                 |
| text             | optional  | n/a     | input text, if present -- if absent, input is the request body  |

Other API validation rules:
- Only one of `characterMapName` or `characterMap` may exist.
- Only one of `keyboardName` or `keyboard` may exists.
- Either `reflectorName` or `reflector` (but not both) must exist.
- In mapping objects, either `mapping` or `codes` (but not both) must exist.
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

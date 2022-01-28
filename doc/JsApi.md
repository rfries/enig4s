# MachineRequest

```
    {
      "busSize": 26
      "characterMapName": "AZ",
      "characterMap": {
        "mapping": "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
        "codes": [65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90]
      }
      "keyboardName": "AZ",
      "keyboardCodes": [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25],
      "wheels": [
        {
          "name": "I",
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
      }
      "plugs": ["AN", "ST", "ZG", "UH", "KP", "YM"],
      "plugCodes": [[0, 13], [18, 19], [25, 6], [20, 7], [10, 15], [24, 12]],
      "text": "THIS IS A TEST MESSAGE"
    }
  """

  val basicJson2 = """
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
          "wheelName": "II"
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
  """

```

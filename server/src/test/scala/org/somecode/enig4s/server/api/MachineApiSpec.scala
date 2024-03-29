package org.somecode.enig4s
package server
package api

import io.circe.parser.*
class MachineApiSpec {

}

object MachineApiSpec:
  val basicJson = """
    {
      "machineType": "M3",
      "busSize": 26
      "keyboard": "AZ"
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
        }
      ],
      "reflectorName": "UKW-B",
      "plugs": ["AN", "ST", "ZG", "UH", "KP", "YM"],
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

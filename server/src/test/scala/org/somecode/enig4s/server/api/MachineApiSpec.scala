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

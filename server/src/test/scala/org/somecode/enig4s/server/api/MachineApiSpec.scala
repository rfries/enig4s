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
      { "wheelName": "I" },
      { "wheelName": "II" },
      { "mapping": "YZABCDEFGHIJKLMNOPQRSTUVWX", "notches": "CJ" }
    ],
    "reflectorName": "UKW-B",
    "ringSettings": "PQL",
    "wheelPositions": "AHM",
    "plugs": ["AN", "ST", "ZG", "UH", "KP", "YM"],
    "text": "THIS IS A TEST MESSAGE"
  }
"""

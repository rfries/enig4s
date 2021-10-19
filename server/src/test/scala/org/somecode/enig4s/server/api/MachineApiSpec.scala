package org.somecode.enig4s.server.api

class MachineApiSpec {

}

object MachineApiSpec:
  val basicJson = """
  {
    "machineType": "M3",
    "wheels": [
      { "wheelName": "I" },
      { "wheelName": "II" },
      { "wheel": "YZABCDEFGHIJKLMNOPQRSTUVWX" }
    ],
    "reflectorName": "UKW-B",
    "ringSettings": "PQL",
    "wheelPositions": "AHM",
    "plugs": ["AN", "ST", "ZG", "UH", "KP", "YM"],
    "text": "THIS IS A TEST MESSAGE"
  }
  """

package org.somecode.enig4s
package mach

trait Transformer {
  /**
    * Returns a function that takes a tuple of a [[Glyph]] and
    * [[MachineState]], and returns a transformed [[Glyph]] and an updated
    * [[MachineState]].
    */
  def transformer: (MachineState, Glyph) => (MachineState, Glyph)
}

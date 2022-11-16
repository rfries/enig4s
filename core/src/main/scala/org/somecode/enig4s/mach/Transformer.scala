package org.somecode.enig4s
package mach

/**
 * Represents a function that can transform a [[Glyph]] based on
 * a [[MachineState]], returning the transformed [[Glyph]] and an
 * updated [[MachineState]].
 */
type Transformer = ((MachineState, Glyph)) => (MachineState, Glyph)

package org.somecode.enig4s
package mach

/**
 * Represents the ring setting of a [[Wheel]]; that is, a value that is subtracted from
 * a [[Glyph]] when being transformed by a [[Wheel]]. `RingSetting` is a tiny `Int` type
 * limited to positive integers and 0.
 */

opaque type RingSetting <: Int = Int
object RingSetting extends NewPozNum[RingSetting, Int]
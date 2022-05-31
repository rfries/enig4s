package org.somecode.enig4s
package mach

/**
 * Represents the position of a rotor; that is, a value that is added to a [[KeyCode]]
 * when being transformed by a [[Wheel]]. `Position` is a tiny `Int` type
 * limited to positive integers and 0.
 */

opaque type Position <: Int = Int
object Position extends NewPozNum[Position, Int]

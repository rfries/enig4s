package org.somecode.enig4s
package mach

import cats.data.{Chain, Writer}

import scala.collection.immutable.Queue

/**
 * Represents a function that can transform a [[Glyph]] based on
 * a [[MachineState]], returning the transformed [[Glyph]] and an
 * updated [[MachineState]].
 */
type Transformer = ((MachineState, Glyph)) => Writer[Chain[String], Glyph]

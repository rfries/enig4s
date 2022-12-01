package org.somecode.enig4s
package mach

/**
 * Represents a bi-directional transformer, which can provide
 * a transform function in both the forward and reverse (post-
 * reflector) directions.
 */

trait Bidirectional:
  def forward: Transformer
  def reverse: Transformer

package org.somecode.enig4s
package mach

import org.scalatest.matchers.should
import org.scalatest.wordspec.AnyWordSpec
import scala.collection.immutable.ArraySeq

trait Enig4sSpec extends AnyWordSpec with should.Matchers:
  def glyphs(ints: Int*): ArraySeq[Glyph] = ints.map(Glyph.unsafe).to(ArraySeq)

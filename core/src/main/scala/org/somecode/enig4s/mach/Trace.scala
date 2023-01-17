package org.somecode.enig4s
package mach

object Trace:

  enum Direction(val dir: String):
    case Forward extends Direction("in")
    case Reverse extends Direction("out")

  enum Component(val name: String):
    case EntryDisc(dir: Direction) extends Component(s"Entry[${dir.dir}]")
    case Wheel(num: Int)           extends Component(s"Wheel[$num]")
    case Reflector                 extends Component("Reflect")
    case Plugboard(dir: Direction) extends Component(s"Plugs[${dir.dir}]")

  def trace(state: MachineState,
    inGlyph: Glyph,
    outGlyph: Glyph,
    component: Component,
    extra: String = ""): (MachineState, Glyph) =

    val newState = state.traceQ.map { q =>
      val in = state.
        symbols.displayGlyph(inGlyph)
      val out = state.symbols.displayGlyph(outGlyph)
      val glyphs = s"$in ==> $out"
      val msg = f"${component.name}%-10s $glyphs $extra"
      state.copy(traceQ = Some(q.enqueue(msg)))
    }
    (newState.getOrElse(state), outGlyph)

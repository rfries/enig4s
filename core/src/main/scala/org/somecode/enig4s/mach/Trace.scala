package org.somecode.enig4s
package mach

object Trace:

  enum Direction(val shortArrow: String, val longArrow: String):
    case Forward extends Direction("<-", "<==")
    case Reverse extends Direction("->", "==>")
    case Reflect extends Direction("><", ">>>")

  enum Component(val shortName: String):
    case EntryDisc        extends Component("Entry")
    case Wheel(num: Int)  extends Component(s"Wheel[$num]")
    case Reflector        extends Component("Reflect")
    case Plugboard        extends Component("Plugboard")

  def trace(state: MachineState,
    inGlyph: Glyph,
    outGlyph: Glyph,
    component: Component,
    direction: Direction,
    extra: String = ""): (MachineState, Glyph) =

    val newState = state.traceQ.map { q =>
      val in = state.symbols.displayCode(inGlyph)
      val out = state.symbols.displayCode(outGlyph)

      val glyphs =
        if direction == Direction.Reverse || direction == Direction.Reflect then
          s"$in ${direction.longArrow} $out"
        else
          f"$out ${direction.longArrow} $in"

      val msg = f"${direction.shortArrow} ${component.shortName}%-9s $glyphs $extra"
      state.copy(traceQ = Some(q.enqueue(msg)))
    }
    (newState.getOrElse(state), outGlyph)


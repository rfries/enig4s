//import org.somecode.enig4s.mach.TypeXPlugBoard
import org.somecode.enig4s.mach.Wiring
import org.somecode.enig4s.mach.EnigmaPlugBoard
import org.somecode.enig4s.mach.SymbolMap

for
  syms <- SymbolMap("ABCD")
  plugs = Vector("AD")
  in <- syms.stringToCodes("ABCD")
  plugboard <- EnigmaPlugBoard(4, plugs, syms)
yield
  syms.glyphsToString(in.map(plugboard.forward))


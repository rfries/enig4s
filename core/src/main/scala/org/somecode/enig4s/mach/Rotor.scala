package org.somecode.enig4s.mach

trait Rotor:
  def wiring: Wiring

//  def forward(state: WheelState, in: KeyCode): KeyCode => KeyCode = { in =>
//    val out: KeyCode = minusMod(wiring.translate(plusMod(in, state)), state)
//    println(f"${state.wheelNum}: [${state.position}%02d] $in%02d (${(in + 'A').toChar}) -> $out%02d (${(out + 'A').toChar})")
//    out
//  }

  def plusMod(in: KeyCode, state: WheelState): KeyCode = normalMod(in + state.offset)
  def plusMod(in: KeyCode, pos: Position): KeyCode = normalMod(in + pos)
  def minusMod(in: KeyCode, state: WheelState): KeyCode = normalMod(in - state.offset)
  def minusMod(in: KeyCode, pos: Position): KeyCode = normalMod(in - pos)

  def normalMod(n: Int): KeyCode =
    val res = n % wiring.size
    val out = if res < 0 then wiring.size + res else res
    KeyCode.unsafe(out)

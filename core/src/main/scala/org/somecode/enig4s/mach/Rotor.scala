package org.somecode.enig4s.mach

trait Rotor:
  def size: Int

  def plusMod(in: KeyCode, state: WheelState): KeyCode = normalMod(in + state.offset)
  def plusMod(in: KeyCode, pos: Position): KeyCode = normalMod(in.toInt + pos.toInt)
  def minusMod(in: KeyCode, state: WheelState): KeyCode = normalMod(in - state.offset)
  def minusMod(in: KeyCode, pos: Position): KeyCode = normalMod(in.toInt - pos.toInt)

  def normalMod(n: Int): KeyCode =
    val res = n % size
    val out = if res < 0 then size + res else res
    KeyCode.unsafe(out)

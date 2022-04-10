package org.somecode.enig4s.mach

abstract class NewString[S <: String]:

  def apply(s: S): Either[String, S]

  protected final def apply(s: S, cond: Boolean, desc: String): Either[String, S] =
    Either.cond(cond, s, desc)

  final def unsafe(s: S): S = apply(s).fold(
    e => throw new IllegalArgumentException(e),
    v => v)

class NewNonEmptyString[S <: String] extends NewString[S]:
  final override def apply(s: S): Either[String, S] =
    apply(s, s.nonEmpty, "String cannot be empty.")

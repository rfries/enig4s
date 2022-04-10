package org.somecode.enig4s
package mach

import cats.Eq

abstract class NewNum[T, N <: T](using num: Numeric[N], eq: Eq[N]):
  val zero: T = num.zero
  val one: T = num.one

  def apply(n: N): Either[String, T]

  protected def apply(n: N, cond: Boolean, desc: String): Either[String, T] =
    Either.cond(cond, n, s"$n is not $desc")

  final def unsafe(n: N): T = apply(n).fold(
    s => throw new IllegalArgumentException(s),
    v => v)

  given Eq[T] = Eq.fromUniversalEquals[T]

class NewPozNum[T, N <: T](using num: Numeric[N], eq: Eq[N]) extends NewNum[T, N]:
  final override def apply(n: N): Either[String, T] =
    apply(n, num.gteq(n, num.zero), ">= 0")


class NewPosNum[T, N <: T](using num: Numeric[N], eq: Eq[N]) extends NewNum[T, N]:
  final override def apply(n: N): Either[String, T] =
    apply(n, num.gt(n, num.zero), "> 0")

class NewPozMaxNum[T, N <: T](max: N)(using num: Numeric[N], eq: Eq[N]) extends NewNum[T, N]:
  final override def apply(n: N): Either[String, T] =
    apply(n, num.gteq(n, num.zero) && num.lteq(n, max), s">= 0 && <= $max")

class NewPosMaxNum[T, N <: T](max: N)(using num: Numeric[N], eq: Eq[N]) extends NewNum[T, N]:
  final override def apply(n: N): Either[String,T] =
    apply(n, num.gt(n, num.zero) && num.lteq(n, max), s"> 0 && <= $max")

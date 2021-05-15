package org.somecode.enigma

// Common package-level definitions (formally in the package object)

extension[R] (e: Either[String, R])
  def require: R = e.fold(
    s => throw IllegalArgumentException(s"Required value not available [$s]."),
    p => p)

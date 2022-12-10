
## Collections Usage

Wheels, Reflectors, and Wiring do lots of indexed lookups, so they should use
IndexedSeqs.  However, since there is no Applicative instance for IndexedSeq,
and traverse/sequence is 80% of why I use cats, I use a concrete class
(ArraySeq) in a number of places to prevent the need for conversion when
doing traverse/sequence.

The trace log uses a Queue, as it has constant append time (and is a bit underused, IMO)

I use typelevel CIString for case-insensitive string keys in the Cabinet, as it just works
and includes some useful cats instances.

## Error Types

Generally, errors are expressed as Either[String, A].  This is mostly an expediency, and in larger
or more enterprisey software I would recommend the use an ADT instead of String.
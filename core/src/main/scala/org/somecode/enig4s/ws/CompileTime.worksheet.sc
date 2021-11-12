import scala.compiletime.ops.int.*

type IsPosInt[N <: Int] = >=[N,  0]
type IsPosInt2 = [N <: Int] =>> N >= 0

val p1 : IsPosInt[3] = true
val p2 : IsPosInt[-3] = false
val p3 : IsPosInt2[3] = true
val p4 : IsPosInt2[-3] = false

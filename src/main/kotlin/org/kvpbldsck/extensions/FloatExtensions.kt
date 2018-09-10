package org.kvpbldsck.extensions

private const val DEFAULT_EQUALITY_DELTA = (1e-5).toFloat()

fun Float.isLessThan(other : Float, equalityDelta : Float = DEFAULT_EQUALITY_DELTA)
        = this - other < equalityDelta

fun Float.isGreaterThan(other : Float, equalityDelta : Float = DEFAULT_EQUALITY_DELTA)
        = this - other > equalityDelta

fun Float.isEqualsTo(other : Float, equalityDelta : Float = DEFAULT_EQUALITY_DELTA)
        = !isLessThan(other, equalityDelta) && !isGreaterThan(other, equalityDelta)
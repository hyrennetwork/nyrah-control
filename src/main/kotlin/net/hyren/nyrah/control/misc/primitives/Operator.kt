package net.hyren.nyrah.control.misc.primitives

/**
 * @author Gutyerrez
 */
infix fun Byte.and(other: Int) = toInt() and other

infix fun Int.or(other: Byte) = other.toInt() or this
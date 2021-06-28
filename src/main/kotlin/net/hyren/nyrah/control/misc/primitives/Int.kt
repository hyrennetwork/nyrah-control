package net.hyren.nyrah.control.misc.primitives

/**
 * @author Gutyerrez
 */
fun Int.getVarIntSize() = when {
    this and 0xFFFFFF80.toInt() == 0x0 -> 1
    this and 0xFFFFC000.toInt() == 0x0 -> 2
    this and 0xFFE00000.toInt() == 0x0 -> 3
    this and 0xF0000000.toInt() == 0x0 -> 4
    else -> 5
}
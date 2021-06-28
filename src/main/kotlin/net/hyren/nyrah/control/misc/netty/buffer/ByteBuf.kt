package net.hyren.nyrah.control.misc.netty.buffer

import io.netty.buffer.ByteBuf

/**
 * @author Gutyerrez
 */
fun ByteBuf.readVarInt(maxSize: Int = 5): Int {
    var output = 0
    var current = 0

    do {
        val byte = readByte()

        output = output or (byte.toInt() and 0x7F) shl current++ * 7

        if (current > maxSize) {
            throw RuntimeException("VarInt too big")
        }

    } while ((byte.toInt() and 0x80) == 0x80)

    return output
}

fun ByteBuf.writeVarInt(int: Int) {
    var part: Int
    var value = int

    do {
        value = value ushr 7
        part = value and 0x7F

        if (value != 0x00) {
            part = part or 0x80
        }

        writeByte(part)
    } while (value != 0x00)
}

fun ByteBuf.readString(maxLength: Int = 32767 * 4): String {
    val length = readVarInt()

    if (length < 0) {
        throw RuntimeException("Length cannot be less than 0")
    }

    val byteArray = ByteArray(length)

    readBytes(byteArray)

    val string = String(byteArray)

    return if (string.length > maxLength) {
        throw RuntimeException("Cannot receive string longer than $maxLength (got ${string.length} characters)")
    } else {
        string
    }
}

fun ByteBuf.writeString(string: String?, maxLength: Int = 32767) {
    if (string == null) {
        throw RuntimeException("Cannot write null string")
    }

    if (string.length > maxLength) {
        throw RuntimeException("Cannot send string longer than $maxLength (got ${string.length} characters)")
    }

    val byteArray = string.toByteArray(Charsets.UTF_8)

    writeVarInt(byteArray.size)
    writeBytes(byteArray)
}
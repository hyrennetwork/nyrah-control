package net.hyren.nyrah.control.misc.netty.buffer

import io.netty.buffer.ByteBuf
import kotlin.properties.Delegates
import net.hyren.nyrah.control.misc.primitives.and

/**
 * @author Gutyerrez
 */
fun ByteBuf.readVarInt(): Int {
    var output = 0
    var bytes = 0

    do {
        val byte = readByte()

        output = output or ((byte and 0x7F) shl (bytes++ * 7))

        if (bytes > 5) {
            throw RuntimeException("VarInt too big")
        }
    } while (byte and 0x80 == 0x80)

    return output
}

fun ByteBuf.writeVarInt(int: Int) {
    var part by Delegates.notNull<Int>()

    var value = int

    do {
        part = value and 0x7F
        value = value shr 7

        if (value != 0) {
            part = part or 0x80
        }

        writeByte(part)
    } while (value != 0)
}

fun ByteBuf.readString(maxLength: Int = 32767 * 4): String {
    val length = readVarInt()

    println("Length da string: $length")

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

    println("Size da bytearray para escrever ${byteArray.size}")

    writeVarInt(byteArray.size)
    writeBytes(byteArray)
}
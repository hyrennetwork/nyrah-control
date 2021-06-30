package net.hyren.nyrah.control.misc.vertx.buffer

import io.netty.handler.codec.CorruptedFrameException
import io.vertx.core.buffer.Buffer
import net.hyren.nyrah.control.misc.primitives.and

/**
 * @author Gutyerrez
 */
fun Buffer.readVarInt(): Int {
    var output = 0
    var current = -1

    while (length() > 0) {
        if (++current > 2) {
            throw RuntimeException("Packet size is wider than 21 bits")
        }

        val byte = byteBuf.getByte(current)

        output = output or ((byte and 0x7F) shl current * 7)

        if ((byte and 0x80) == 0) {
            return output
        }

        if (output == 0x00) {
            throw CorruptedFrameException("Packet size cannot be 0")
        }
    }

    return -1
}
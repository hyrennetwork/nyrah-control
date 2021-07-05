package net.hyren.nyrah.control.misc.protocol.packet.implementations

import io.netty.buffer.ByteBuf
import net.hyren.nyrah.control.handler.IHandler
import net.hyren.nyrah.control.misc.netty.buffer.readString
import net.hyren.nyrah.control.misc.netty.buffer.writeString
import net.hyren.nyrah.control.misc.primitives.getVarIntSize
import net.hyren.nyrah.control.misc.protocol.packet.IPacket

/**
 * @author Gutyerrez
 */
class DisconnectPacket(
    var reason: String? = null
) : IPacket {

    override fun read(
        byteBuf: ByteBuf
    ) { reason = byteBuf.readString() }

    override fun write(
        byteBuf: ByteBuf
    ) = byteBuf.writeString(reason)

    override fun handle(
        handler: IHandler
    ) = Unit

    override fun size() = reason?.let {
        it.toByteArray().size.getVarIntSize() + it.toByteArray().size
    } ?: 0

}
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
class StatusResponsePacket(
    var response: String = ""
) : IPacket {

    override fun read(
        byteBuf: ByteBuf
    ) {
        response = byteBuf.readString()
    }

    override fun write(
        byteBuf: ByteBuf
    ) = byteBuf.writeString(response)

    override fun handle(
        handler: IHandler
    ) = Unit

    override fun size() = /*response.toByteArray().size.getVarIntSize() + response.toByteArray().size*/9211

}
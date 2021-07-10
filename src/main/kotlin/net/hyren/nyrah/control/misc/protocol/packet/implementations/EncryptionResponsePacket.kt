package net.hyren.nyrah.control.misc.protocol.packet.implementations

import io.netty.buffer.ByteBuf
import net.hyren.nyrah.control.handler.IHandler
import net.hyren.nyrah.control.misc.protocol.packet.IPacket

/**
 * @author Gutyerrez
 */
class EncryptionResponsePacket : IPacket {

    override fun read(
        byteBuf: ByteBuf
    ) {
        println("Read encrypt")
    }

    override fun write(
        byteBuf: ByteBuf
    ) {
        println("Write encrypt")
    }

    override fun handle(
        handler: IHandler
    ) {
        println("Encryption response")
    }

    override fun size() = 0

}
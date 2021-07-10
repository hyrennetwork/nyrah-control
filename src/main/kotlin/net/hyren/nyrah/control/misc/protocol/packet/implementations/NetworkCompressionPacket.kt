package net.hyren.nyrah.control.misc.protocol.packet.implementations

import io.netty.buffer.ByteBuf
import net.hyren.nyrah.control.handler.IHandler
import net.hyren.nyrah.control.misc.netty.buffer.readVarInt
import net.hyren.nyrah.control.misc.netty.buffer.writeVarInt
import net.hyren.nyrah.control.misc.protocol.packet.IPacket
import kotlin.properties.Delegates

/**
 * @author Gutyerrez
 */
class NetworkCompressionPacket : IPacket {

    var threshold by Delegates.notNull<Int>()

    override fun read(
        byteBuf: ByteBuf
    ) { threshold = byteBuf.readVarInt() }

    override fun write(
        byteBuf: ByteBuf
    ) = byteBuf.writeVarInt(threshold)

    override fun handle(
        handler: IHandler
    ) {
        println("Compression")
    }

    override fun size() = threshold

}
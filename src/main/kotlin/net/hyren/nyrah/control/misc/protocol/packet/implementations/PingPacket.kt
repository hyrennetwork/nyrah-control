package net.hyren.nyrah.control.misc.protocol.packet.implementations

import io.netty.buffer.ByteBuf
import net.hyren.nyrah.control.handler.IHandler
import net.hyren.nyrah.control.misc.protocol.packet.IPacket
import kotlin.properties.Delegates

/**
 * @author Gutyerrez
 */
class PingPacket : IPacket {

    private var payload by Delegates.notNull<Long>()

    override fun read(
        byteBuf: ByteBuf
    ) {
        payload = byteBuf.readLong()
    }

    override fun write(
        byteBuf: ByteBuf
    ) {
        byteBuf.writeLong(payload)
    }

    override fun handle(
        handler: IHandler
    ) {
        handler.sendPacket(this).onComplete { handler.socket.close() }
    }

    override fun size() = 8

}
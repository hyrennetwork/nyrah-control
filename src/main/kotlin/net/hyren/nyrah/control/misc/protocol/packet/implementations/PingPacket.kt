package net.hyren.nyrah.control.misc.protocol.packet.implementations

import io.netty.buffer.ByteBuf
import kotlin.properties.Delegates
import net.hyren.nyrah.control.handler.IHandler
import net.hyren.nyrah.control.misc.protocol.packet.IPacket

/**
 * @author Gutyerrez
 */
class PingPacket : IPacket {

    private var payload by Delegates.notNull<Long>()

    override fun read(
        byteBuf: ByteBuf
    ) {
        println("Read ping")

        payload = byteBuf.readLong()
    }

    override fun write(
        byteBuf: ByteBuf
    ) {
        println("Write ping")

        byteBuf.writeLong(payload)
    }

    override fun handle(
        handler: IHandler
    ) {
        println("Ping!")

        handler.sendPacket(this).onComplete { handler.socket.close() }
    }

    override fun size() = 8

}
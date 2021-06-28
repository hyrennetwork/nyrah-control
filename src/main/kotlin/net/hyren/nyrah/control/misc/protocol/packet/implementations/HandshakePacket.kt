package net.hyren.nyrah.control.misc.protocol.packet.implementations

import io.netty.buffer.ByteBuf
import kotlin.properties.Delegates
import net.hyren.nyrah.control.handler.IHandler
import net.hyren.nyrah.control.misc.netty.buffer.readString
import net.hyren.nyrah.control.misc.netty.buffer.readVarInt
import net.hyren.nyrah.control.misc.protocol.Protocol
import net.hyren.nyrah.control.misc.protocol.packet.IPacket

/**
 * @author Gutyerrez
 */
class HandshakePacket : IPacket {

    var protocolVersion by Delegates.notNull<Int>()
    var serverAddress by Delegates.notNull<String>()
    var serverPort by Delegates.notNull<Int>()
    var requestedProtocol by Delegates.notNull<Int>()

    override fun read(
        byteBuf: ByteBuf
    ) {
        protocolVersion = byteBuf.readVarInt()
        serverAddress = byteBuf.readString()
        serverPort = byteBuf.readUnsignedShort()
        requestedProtocol = byteBuf.readVarInt()
    }

    override fun write(
        byteBuf: ByteBuf
    ) {
        //
    }

    override fun handle(
        handler: IHandler
    ) {
        when (requestedProtocol) {
            1 -> handler.protocol = Protocol.STATUS
            2 -> {
                // game
            }
        }
    }

    override fun size() = 1029

}
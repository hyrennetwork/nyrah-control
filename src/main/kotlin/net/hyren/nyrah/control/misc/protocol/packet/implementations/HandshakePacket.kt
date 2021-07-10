package net.hyren.nyrah.control.misc.protocol.packet.implementations

import io.netty.buffer.ByteBuf
import net.hyren.core.shared.CoreConstants
import net.hyren.nyrah.control.handler.IHandler
import net.hyren.nyrah.control.handler.implementations.InitialHandler
import net.hyren.nyrah.control.misc.netty.buffer.readString
import net.hyren.nyrah.control.misc.netty.buffer.readVarInt
import net.hyren.nyrah.control.misc.netty.buffer.writeString
import net.hyren.nyrah.control.misc.netty.buffer.writeVarInt
import net.hyren.nyrah.control.misc.protocol.Protocol
import net.hyren.nyrah.control.misc.protocol.packet.IPacket
import net.md_5.bungee.api.chat.ComponentBuilder
import java.net.InetSocketAddress
import kotlin.properties.Delegates

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
        byteBuf.writeVarInt(protocolVersion)
        byteBuf.writeString(serverAddress)
        byteBuf.writeVarInt(serverPort)
        byteBuf.writeVarInt(requestedProtocol)
    }

    override fun handle(
        handler: IHandler
    ) {
        handler.setRawProtocolVersion(protocolVersion)

        when (requestedProtocol) {
            1 -> handler.protocol = Protocol.STATUS
            2 -> {
                if (protocolVersion != 47) {
                    handler.close(
                        ComponentBuilder()
                            .append(CoreConstants.Info.ERROR_SERVER_NAME)
                            .append("\n\n")
                            .append("§cA versão do seu Minecraft não é suportada pelo servidor.")
                            .create()
                    )
                    return
                }

                if (handler is InitialHandler) {
                    handler._address = InetSocketAddress(
                        serverAddress,
                        serverPort
                    )
                }

                handler.protocol = Protocol.LOGIN
            }
        }
    }

    override fun size() = 1029

}
package net.hyren.nyrah.control.handler.implementations

import io.vertx.core.Vertx
import io.vertx.core.net.NetSocket
import net.hyren.nyrah.control.handler.IHandler
import net.hyren.nyrah.control.misc.protocol.Protocol
import net.hyren.nyrah.control.misc.protocol.packet.implementations.HandshakePacket
import net.hyren.nyrah.control.misc.protocol.packet.implementations.LoginSuccessPacket

/**
 * @author Gutyerrez
 */
class ConnectionHandler(
    initialHandler: IHandler,
    override val socket: NetSocket
) : IHandler {

    override val VERTX: Vertx = Vertx.currentContext().owner()

    override var isClosed = false
    override var isOnlineMode = false

    override var protocol = Protocol.HANDSHAKE

    override var opposite: IHandler? = initialHandler

    init {
        sendPacket(
            HandshakePacket().apply {
                protocolVersion = opposite!!.getRawProtocolVersion()
                serverAddress = "localhost"
                serverPort = 25565
                requestedProtocol = 2
            }
        ).onSuccess {
            protocol = Protocol.LOGIN

            sendPacket(LoginSuccessPacket().apply {
                username = opposite!!.getUser()!!.name
                uniqueId = opposite!!.getUser()!!.getUniqueId()
            })
        }.onFailure { throw it }
    }

}
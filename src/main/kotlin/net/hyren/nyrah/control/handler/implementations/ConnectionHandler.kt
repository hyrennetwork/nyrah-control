package net.hyren.nyrah.control.handler.implementations

import io.vertx.core.Vertx
import io.vertx.core.net.NetSocket
import net.hyren.nyrah.control.handler.IHandler
import net.hyren.nyrah.control.misc.protocol.Protocol
import net.hyren.nyrah.control.misc.protocol.packet.implementations.HandshakePacket
import net.hyren.nyrah.control.misc.protocol.packet.implementations.NyrahLoginRequestPacket

/**
 * @author Gutyerrez
 */
class ConnectionHandler(
    handler: IHandler,
    override val socket: NetSocket
) : IHandler {

    override val VERTX: Vertx = Vertx.currentContext().owner()

    override var isClosed = false
    override var isOnlineMode = false

    override var protocol = Protocol.HANDSHAKE

    override var opposite: IHandler? = handler

    // proxy 1 = 135.148.70.166:30001

    init {
        sendPacket(
            HandshakePacket().apply {
                protocolVersion = opposite!!.getRawProtocolVersion()
                serverAddress = "135.148.70.166"
                serverPort = 30001
                requestedProtocol = 2
            }
        ).onSuccess {
            protocol = Protocol.LOGIN

            val user = opposite?.getUser()!!

            sendPacket(NyrahLoginRequestPacket(
                user.name,
                user.getUniqueId(),
                false,
                opposite?.getAddress()!!
            )).onSuccess { println("Enviei o packet de login - nyrah") }.onFailure { throw it }
        }.onFailure { throw it }
    }

    override fun getAddress() = opposite!!.getAddress()

    override fun Protocol.getOutboundDirection() = TO_SERVER

    override fun Protocol.getInboundDirection() = TO_CLIENT

}
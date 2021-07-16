package net.hyren.nyrah.control.connection

import io.vertx.core.Vertx
import io.vertx.core.net.NetSocket
import net.hyren.core.shared.applications.data.Application
import net.hyren.nyrah.control.handler.IHandler
import net.hyren.nyrah.control.handler.implementations.AbstractPacketHandler
import net.hyren.nyrah.control.misc.minecraft.VarIntPrefixedDelimiter
import net.hyren.nyrah.control.misc.protocol.Protocol
import net.hyren.nyrah.control.misc.protocol.packet.implementations.HandshakePacket
import net.hyren.nyrah.control.misc.protocol.packet.implementations.NyrahLoginRequestPacket

/**
 * @author Gutyerrez
 */
class UserConnectionHandler(
    handler: IHandler,
    override val socket: NetSocket
) : AbstractPacketHandler() {

    override val VERTX: Vertx = Vertx.currentContext().owner()

    override var isClosed = false
    override var isOnlineMode = false

    override var protocol = Protocol.HANDSHAKE

    override var opposite: IHandler? = handler

    lateinit var proxy: Application

    init {
        socket.closeHandler { println("fecharam") }.handler(
            VarIntPrefixedDelimiter(
                this
            ) { handle(it) }
        )

        sendPacket(
            HandshakePacket().apply {
                protocolVersion = opposite!!.getRawProtocolVersion()
                serverAddress = "45.231.208.180"
                serverPort = 25565
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
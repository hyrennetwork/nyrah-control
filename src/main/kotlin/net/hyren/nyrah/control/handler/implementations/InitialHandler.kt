package net.hyren.nyrah.control.handler.implementations

import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.core.net.NetSocket
import net.hyren.nyrah.control.handler.IHandler
import net.hyren.nyrah.control.misc.minecraft.VarIntPrefixedDelimiter
import net.hyren.nyrah.control.misc.protocol.Protocol
import net.hyren.nyrah.control.misc.protocol.packet.implementations.DisconnectPacket
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.chat.ComponentSerializer

/**
 * @author Gutyerrez
 */
class InitialHandler(
    override val socket: NetSocket
) : IHandler {

    override val VERTX: Vertx = Vertx.currentContext().owner()

    override var isClosed = false
    override var isOnlineMode = false

    override var protocol = Protocol.HANDSHAKE

    override var opposite: IHandler? = null

    init {
        val handler = VarIntPrefixedDelimiter(
            this
        ) { handle(it) }

        socket.handler(handler)
    }

    override fun close() {
        socket.close()

        isClosed = true
    }

    override fun close(
        reason: Array<BaseComponent>
    ): Future<Void> = sendPacket(
        DisconnectPacket(
            ComponentSerializer.toString(
                *reason
            )
        )
    ).onComplete { socket.close() }

}
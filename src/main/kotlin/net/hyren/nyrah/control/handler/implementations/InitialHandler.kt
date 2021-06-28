package net.hyren.nyrah.control.handler.implementations

import io.vertx.core.net.NetSocket
import net.hyren.nyrah.control.handler.IHandler
import net.hyren.nyrah.control.misc.minecraft.VarIntPrefixedDelimiter
import net.hyren.nyrah.control.misc.protocol.Protocol

/**
 * @author Gutyerrez
 */
class InitialHandler(
    override val socket: NetSocket
): IHandler {

    override var isClosed = false
    override var isOnlineMode = false

    override var protocol = Protocol.HANDSHAKE

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

}
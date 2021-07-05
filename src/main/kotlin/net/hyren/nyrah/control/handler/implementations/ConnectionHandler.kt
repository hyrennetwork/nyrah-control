package net.hyren.nyrah.control.handler.implementations

import io.vertx.core.Vertx
import io.vertx.core.net.NetSocket
import net.hyren.nyrah.control.handler.IHandler
import net.hyren.nyrah.control.misc.protocol.Protocol

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

}
package net.hyren.nyrah.control.handler.implementations

import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.core.net.NetSocket
import net.hyren.core.shared.users.data.User
import net.hyren.nyrah.control.handler.IHandler
import net.hyren.nyrah.control.misc.minecraft.VarIntPrefixedDelimiter
import net.hyren.nyrah.control.misc.protocol.Protocol
import net.hyren.nyrah.control.misc.protocol.packet.implementations.DisconnectPacket
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.chat.ComponentSerializer
import java.net.InetSocketAddress
import kotlin.properties.Delegates

/**
 * @author Gutyerrez
 */
class InitialHandler(
    override val socket: NetSocket
) : AbstractPacketHandler() {

    override val VERTX: Vertx = Vertx.currentContext().owner()

    override var isClosed = false
    override var isOnlineMode = false

    override var protocol = Protocol.HANDSHAKE

    override var opposite: IHandler? = null

    private var protocolVersion by Delegates.notNull<Int>()

    private var _user: User? = null

    var _address by Delegates.notNull<InetSocketAddress>()

    init {
        socket.handler(
            VarIntPrefixedDelimiter(
                this
            ) { handle(it) }
        )
    }

    override fun setUser(
        user: User
    ) {
        _user = user
    }

    override fun getUser() = _user

    override fun setRawProtocolVersion(
        protocolVersion: Int
    ) { this.protocolVersion = protocolVersion }

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
    ).onComplete {
        socket.close()

        isClosed = true
    }

    override fun getAddress() = _address

}
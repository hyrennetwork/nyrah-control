package net.hyren.nyrah.control.handler

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufAllocator
import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.core.net.NetSocket
import net.hyren.core.shared.users.data.User
import net.hyren.nyrah.control.misc.netty.buffer.readVarInt
import net.hyren.nyrah.control.misc.netty.buffer.writeVarInt
import net.hyren.nyrah.control.misc.primitives.getVarIntSize
import net.hyren.nyrah.control.misc.protocol.Protocol
import net.hyren.nyrah.control.misc.protocol.packet.IPacket
import net.md_5.bungee.api.chat.BaseComponent

/**
 * @author Gutyerrez
 */
interface IHandler {

    val VERTX: Vertx

    val socket: NetSocket

    var isClosed: Boolean
    var isOnlineMode: Boolean

    var protocol: Protocol

    var opposite: IHandler?

    fun handle(
        byteBuf: ByteBuf
    ) {
        val packetId = byteBuf.readVarInt()

        byteBuf.markReaderIndex()

        val packet = protocol.TO_SERVER.createPacket(packetId) ?: return

        packet.read(byteBuf)
        packet.handle(this)
    }

    fun sendPacket(
        packet: IPacket
    ): Future<Void> {
        val packetId = protocol.TO_CLIENT.getPacketId(packet) ?: return Future.failedFuture(
            "Cannot send packet ${packet::class.qualifiedName}"
        )

        val packetSize = packet.size() + packetId.getVarIntSize()

        val byteBuf = ByteBufAllocator.DEFAULT.buffer(packetSize.getVarIntSize() + packetSize)

        byteBuf.writeVarInt(packetSize)
        byteBuf.writeVarInt(packetId)

        packet.write(byteBuf)

        return socket.write(
            Buffer.buffer(byteBuf)
        ).onFailure { throw it }
    }

    fun setUser(
        user: User
    ) = Unit

    fun getUser(): User? = null

    fun setRawProtocolVersion(
        protocolVersion: Int
    ) = Unit

    fun getRawProtocolVersion() = 47

    fun close() = Unit

    fun close(
        reason: Array<BaseComponent>
    ): Future<Void> = Future.succeededFuture()

    fun close(
        reason: BaseComponent
    ) = close(arrayOf(reason))

}
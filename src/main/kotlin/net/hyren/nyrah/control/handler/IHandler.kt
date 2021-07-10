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
import java.net.InetSocketAddress

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

        println("Packet received: $protocol - ${protocol.getInboundDirection()} (#$packetId)")

        val packet = protocol.getInboundDirection().createPacket(packetId) ?: return

        packet.read(byteBuf)
        packet.handle(this)
    }

    fun sendPacket(
        packet: IPacket
    ): Future<Void> {
        val packetId = protocol.getOutboundDirection().getPacketId(packet) ?: return Future.failedFuture(
            "Cannot send packet ${packet::class.qualifiedName}"
        )

        try {
            val packetSize = packet.size() + packetId.getVarIntSize()

            val byteBuf = ByteBufAllocator.DEFAULT.buffer(packetSize.getVarIntSize() + packetSize)

            byteBuf.writeVarInt(packetSize)
            byteBuf.writeVarInt(packetId)

            packet.write(byteBuf)

            println("Send packet: $protocol - ${protocol.getOutboundDirection()} (#$packetId)")

            return socket.write(
                Buffer.buffer(byteBuf)
            ).onFailure { throw it }
        } catch (e: Exception) {
            e.printStackTrace()

            return Future.failedFuture(e)
        }
    }

    fun Protocol.getOutboundDirection() = TO_CLIENT

    fun Protocol.getInboundDirection() = TO_SERVER

    fun getAddress(): InetSocketAddress

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
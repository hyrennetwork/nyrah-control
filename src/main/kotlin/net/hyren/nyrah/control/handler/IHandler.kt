package net.hyren.nyrah.control.handler

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufAllocator
import io.vertx.core.Future
import io.vertx.core.buffer.Buffer
import io.vertx.core.net.NetSocket
import net.hyren.nyrah.control.misc.netty.buffer.readVarInt
import net.hyren.nyrah.control.misc.netty.buffer.writeVarInt
import net.hyren.nyrah.control.misc.primitives.getVarIntSize
import net.hyren.nyrah.control.misc.protocol.Protocol
import net.hyren.nyrah.control.misc.protocol.packet.IPacket

/**
 * @author Gutyerrez
 */
interface IHandler {

    val socket: NetSocket

    var isClosed: Boolean
    var isOnlineMode: Boolean

    var protocol: Protocol

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

    fun close()

}
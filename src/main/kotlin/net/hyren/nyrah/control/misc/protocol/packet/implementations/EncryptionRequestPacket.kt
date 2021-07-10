package net.hyren.nyrah.control.misc.protocol.packet.implementations

import io.netty.buffer.ByteBuf
import net.hyren.nyrah.control.handler.IHandler
import net.hyren.nyrah.control.misc.netty.buffer.readByteArray
import net.hyren.nyrah.control.misc.netty.buffer.readString
import net.hyren.nyrah.control.misc.netty.buffer.writeByteArray
import net.hyren.nyrah.control.misc.netty.buffer.writeString
import net.hyren.nyrah.control.misc.primitives.getVarIntSize
import net.hyren.nyrah.control.misc.protocol.packet.IPacket
import kotlin.properties.Delegates

/**
 * @author Gutyerrez
 */
class EncryptionRequestPacket : IPacket {

    var serverId by Delegates.notNull<String>()
    var publicKey by Delegates.notNull<ByteArray>()
    var verifyToken by Delegates.notNull<ByteArray>()

    override fun read(
        byteBuf: ByteBuf
    ) {
        serverId = byteBuf.readString()
        publicKey = byteBuf.readByteArray()
        verifyToken = byteBuf.readByteArray()

        println("Read encrypt request")
    }

    override fun write(
        byteBuf: ByteBuf
    ) {
        byteBuf.writeString(serverId)
        byteBuf.writeByteArray(publicKey)
        byteBuf.writeByteArray(verifyToken)

        println("Write encrypt request")
    }

    override fun handle(
        handler: IHandler
    ) {
        println("Encriptar o quê?")
    }

    override fun size(): Int {
        val serverId = this.serverId.toByteArray()

        return serverId.size.getVarIntSize() + serverId.size + publicKey.size.getVarIntSize() + publicKey.size + verifyToken.size.getVarIntSize() + verifyToken.size
    }

}
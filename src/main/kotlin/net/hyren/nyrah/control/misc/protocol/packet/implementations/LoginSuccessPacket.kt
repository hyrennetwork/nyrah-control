package net.hyren.nyrah.control.misc.protocol.packet.implementations

import io.netty.buffer.ByteBuf
import net.hyren.nyrah.control.handler.IHandler
import net.hyren.nyrah.control.misc.netty.buffer.readString
import net.hyren.nyrah.control.misc.netty.buffer.writeString
import net.hyren.nyrah.control.misc.primitives.getVarIntSize
import net.hyren.nyrah.control.misc.protocol.packet.IPacket
import java.util.*
import kotlin.properties.Delegates

/**
 * @author Gutyerrez
 */
class LoginSuccessPacket : IPacket {

    var username by Delegates.notNull<String>()
    var uniqueId by Delegates.notNull<UUID>()

    override fun read(
        byteBuf: ByteBuf
    ) {
        username = byteBuf.readString()
        uniqueId = UUID.fromString(byteBuf.readString(
            16
        ))
    }

    override fun write(
        byteBuf: ByteBuf
    ) {
        byteBuf.writeString(username)
        byteBuf.writeString(uniqueId.toString())
    }

    override fun handle(
        handler: IHandler
    ) = Unit

    override fun size() = username.toByteArray().size.getVarIntSize() + username.toByteArray().size + 37

}
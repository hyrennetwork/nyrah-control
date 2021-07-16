package net.hyren.nyrah.control.misc.protocol.packet.implementations

import com.google.common.net.InetAddresses
import io.netty.buffer.ByteBuf
import net.hyren.nyrah.control.handler.IHandler
import net.hyren.nyrah.control.misc.netty.buffer.writeString
import net.hyren.nyrah.control.misc.netty.buffer.writeUUID
import net.hyren.nyrah.control.misc.primitives.getVarIntSize
import net.hyren.nyrah.control.misc.protocol.packet.IPacket
import java.net.InetSocketAddress
import java.util.*
import kotlin.properties.Delegates

/**
 * @author Gutyerrez
 */
class NyrahLoginRequestPacket : IPacket {

    var username by Delegates.notNull<String>()
    var uniqueId by Delegates.notNull<UUID>()
    var isOnlineMode by Delegates.notNull<Boolean>()
    var inetSocketAddress by Delegates.notNull<InetSocketAddress>()

    constructor()

    constructor(
        username: String,
        uniqueId: UUID,
        isOnlineMode: Boolean,
        inetSocketAddress: InetSocketAddress
    ) {
        this.username = username
        this.uniqueId = uniqueId
        this.isOnlineMode = isOnlineMode
        this.inetSocketAddress = inetSocketAddress
    }

    override fun read(
        byteBuf: ByteBuf
    ) = Unit

    override fun write(
        byteBuf: ByteBuf
    ) {
        byteBuf.writeString(username)
        byteBuf.writeUUID(uniqueId)
        byteBuf.writeBoolean(isOnlineMode)
        byteBuf.writeInt(InetAddresses.coerceToInteger(
            inetSocketAddress.address
        ))
        byteBuf.writeShort(inetSocketAddress.port)
    }

    override fun handle(
        handler: IHandler
    ) = Unit

    override fun size() = username.toByteArray().size.getVarIntSize() + username.toByteArray().size + 23

}
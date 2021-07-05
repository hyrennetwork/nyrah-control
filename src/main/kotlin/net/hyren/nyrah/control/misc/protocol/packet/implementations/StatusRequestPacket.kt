package net.hyren.nyrah.control.misc.protocol.packet.implementations

import io.netty.buffer.ByteBuf
import net.hyren.core.shared.CoreProvider
import net.hyren.nyrah.control.handler.IHandler
import net.hyren.nyrah.control.misc.protocol.Protocol
import net.hyren.nyrah.control.misc.protocol.packet.IPacket

/**
 * @author Gutyerrez
 */
class StatusRequestPacket : IPacket {

    override fun read(
        byteBuf: ByteBuf
    ) = Unit

    override fun write(
        byteBuf: ByteBuf
    ) = Unit

    override fun handle(
        handler: IHandler
    ) {
        if (handler.protocol != Protocol.STATUS) {
            return handler.close()
        }

        handler.sendPacket(StatusResponsePacket(
            """
                {
                    "version": {
                        "name": "1.8.9",
                        "protocol": 47
                    },
                    "players": {
                        "max": ${CoreProvider.application.slots},
                        "online": ${CoreProvider.Cache.Redis.USERS_STATUS.provide().fetchUsers().size},
                        "sample": []
                    },
                    "description": {
                        "text": "Nyrah Control"
                    },
                    "favicon": "data:image/png;base64,null"
                }
            """.trimIndent()
        ))
    }

    override fun size() = 0

}
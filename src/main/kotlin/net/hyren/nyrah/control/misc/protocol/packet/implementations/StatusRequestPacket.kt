package net.hyren.nyrah.control.misc.protocol.packet.implementations

import io.netty.buffer.ByteBuf
import net.hyren.core.shared.CoreProvider
import net.hyren.nyrah.control.handler.IHandler
import net.hyren.nyrah.control.misc.protocol.Protocol
import net.hyren.nyrah.control.misc.protocol.packet.IPacket
import java.io.File
import java.util.*

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

        val file = File("favicon.png")

        val favicon = if (!file.exists()) {
            null
        } else {
            Base64.getEncoder().encodeToString(
                file.readBytes()
            )
        }

        handler.sendPacket(
            StatusResponsePacket(
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
                    "favicon": "data:image/png;base64,$favicon"
                }
                """.trimIndent()
            )
        )
    }

    override fun size() = 0

}
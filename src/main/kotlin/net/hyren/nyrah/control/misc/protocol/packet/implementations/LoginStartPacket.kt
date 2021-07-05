package net.hyren.nyrah.control.misc.protocol.packet.implementations

import io.netty.buffer.ByteBuf
import io.vertx.core.net.SocketAddress
import net.hyren.core.shared.CoreConstants
import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.applications.ApplicationType
import net.hyren.core.shared.applications.status.ApplicationStatus
import net.hyren.core.shared.misc.utils.Patterns
import net.hyren.nyrah.control.handler.IHandler
import net.hyren.nyrah.control.handler.implementations.ConnectionHandler
import net.hyren.nyrah.control.misc.netty.buffer.readString
import net.hyren.nyrah.control.misc.netty.buffer.writeString
import net.hyren.nyrah.control.misc.primitives.getVarIntSize
import net.hyren.nyrah.control.misc.protocol.packet.IPacket
import net.md_5.bungee.api.chat.ComponentBuilder
import kotlin.properties.Delegates

/**
 * @author Gutyerrez
 */
class LoginStartPacket : IPacket {

    var name by Delegates.notNull<String>()

    override fun read(
        byteBuf: ByteBuf
    ) { name = byteBuf.readString() }

    override fun write(
        byteBuf: ByteBuf
    ) = byteBuf.writeString(name)

    override fun handle(
        handler: IHandler
    ) {
        if (!Patterns.NICK.matches(name)) {
            println("Opa")
            return handler.close()
        }

        val proxies = CoreProvider.Cache.Local.APPLICATIONS.provide().fetchByApplicationType(
            ApplicationType.PROXY
        ).filter {
            CoreProvider.Cache.Redis.APPLICATIONS_STATUS.provide().fetchApplicationStatusByApplication(
                it,
                ApplicationStatus::class
            ) != null
        }

        if (proxies.isEmpty()) {
            handler.close(
                ComponentBuilder()
                    .append(CoreConstants.Info.ERROR_SERVER_NAME)
                    .append("\n\n")
                    .append("§cNão foi possível localizar um proxy disponível para enviar você.")
                    .create()
            )
        } else {
            val proxyApplication = proxies[
                    CoreProvider.Cache.Redis.USERS_STATUS.provide().fetchUsers().size % proxies.size
            ]

            handler.VERTX.createNetClient().connect(
                SocketAddress.inetSocketAddress(proxyApplication.address)
            ).onSuccess {
                handler.opposite = ConnectionHandler(
                    handler,
                    it
                )
            }.onFailure { throw it }
        }
    }

    override fun size() = name.length.getVarIntSize() + name.length

}
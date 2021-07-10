package net.hyren.nyrah.control.misc.protocol.packet.implementations

import io.netty.buffer.ByteBuf
import io.vertx.core.net.SocketAddress
import net.hyren.core.shared.CoreConstants
import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.applications.ApplicationType
import net.hyren.core.shared.applications.status.ApplicationStatus
import net.hyren.core.shared.misc.utils.Patterns
import net.hyren.core.shared.users.data.User
import net.hyren.core.shared.users.storage.table.UsersTable
import net.hyren.nyrah.control.handler.IHandler
import net.hyren.nyrah.control.handler.implementations.ConnectionHandler
import net.hyren.nyrah.control.misc.netty.buffer.readString
import net.hyren.nyrah.control.misc.netty.buffer.writeString
import net.hyren.nyrah.control.misc.primitives.getVarIntSize
import net.hyren.nyrah.control.misc.protocol.packet.IPacket
import net.md_5.bungee.api.chat.ComponentBuilder
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*
import kotlin.properties.Delegates

/**
 * @author Gutyerrez
 */
class LoginStartPacket : IPacket {

    var username by Delegates.notNull<String>()

    override fun read(
        byteBuf: ByteBuf
    ) { username = byteBuf.readString() }

    override fun write(
        byteBuf: ByteBuf
    ) = byteBuf.writeString(username)

    override fun handle(
        handler: IHandler
    ) {
        if (!Patterns.NICK.matches(username)) {
            handler.close(
                ComponentBuilder()
                    .append(CoreConstants.Info.ERROR_SERVER_NAME)
                    .append("\n\n")
                    .append("§cO nome de usuário não é válido.")
                    .create()
            )

            return
        }

        val proxies = CoreProvider.Cache.Local.APPLICATIONS.provide().fetchByApplicationType(
            ApplicationType.PROXY
        ).filter {
            val applicationStatus = CoreProvider.Cache.Redis.APPLICATIONS_STATUS.provide().fetchApplicationStatusByApplication(
                it,
                ApplicationStatus::class
            ) ?: return@filter false

            return@filter if (it.slots == null) {
                false
            } else {
                applicationStatus.onlinePlayers < it.slots!!
            }
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

            val user = CoreProvider.Cache.Local.USERS.provide().fetchByName(
                username
            ) ?: User(
                EntityID(
                    UUID.nameUUIDFromBytes("OfflinePlayer:$username".toByteArray()),
                    UsersTable
                ),
                username
            )

            handler.setUser(user)

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

    override fun size() = username.length.getVarIntSize() + username.length

}
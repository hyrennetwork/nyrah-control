package net.hyren.nyrah.control.misc.protocol.packet.implementations

import io.netty.buffer.ByteBuf
import net.hyren.nyrah.control.handler.IHandler
import net.hyren.nyrah.control.misc.protocol.Protocol
import net.hyren.nyrah.control.misc.protocol.packet.IPacket
import kotlin.properties.Delegates

/**
 * @author Gutyerrez
 */
class NyrahLoginResponsePacket : IPacket {

    var isSuccess by Delegates.notNull<Boolean>()

    override fun read(
        byteBuf: ByteBuf
    ) { isSuccess = byteBuf.readBoolean() }

    override fun write(
        byteBuf: ByteBuf
    ) = Unit

    override fun handle(
        handler: IHandler
    ) {
        println("Opa")

        handler.protocol = Protocol.GAME
        handler.opposite?.protocol = Protocol.GAME

        println("mover pro play")
    }

    override fun size() = 1

}
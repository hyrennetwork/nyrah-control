package net.hyren.nyrah.control.misc.protocol.packet

import io.netty.buffer.ByteBuf
import net.hyren.nyrah.control.handler.IHandler

/**
 * @author Gutyerrez
 */
interface IPacket {

    fun read(
        byteBuf: ByteBuf
    )

    fun write(
        byteBuf: ByteBuf
    )

    fun handle(
        handler: IHandler
    )

    fun size(): Int

}
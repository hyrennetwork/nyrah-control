package net.hyren.nyrah.control.misc.minecraft

import io.netty.buffer.ByteBuf
import io.vertx.core.Handler
import io.vertx.core.buffer.Buffer
import io.vertx.core.parsetools.RecordParser
import net.hyren.nyrah.control.handler.IHandler
import net.hyren.nyrah.control.misc.primitives.getVarIntSize
import net.hyren.nyrah.control.misc.protocol.Protocol
import net.hyren.nyrah.control.misc.vertx.buffer.readVarInt

/**
 * @author Gutyerrez
 */
class VarIntPrefixedDelimiter(
    val connection: IHandler,
    val handler: (ByteBuf) -> Unit
) : RecordParser {

    private var _buffer: Buffer? = null
    private var _byteSize = -1

    override fun handle(
        buffer: Buffer
    ) {
        println("Connection: ${connection.isClosed}")

        if (!connection.isClosed) {
            try {
                if (_buffer == null || _buffer!!.length() == 0) {
                    _buffer = buffer
                } else {
                    _buffer!!.appendBuffer(buffer)
                }

                if (_buffer!!.length() == 0) {
                    return
                }

                if (connection.protocol == Protocol.HANDSHAKE && _buffer!!.bytes[0].toInt() == -2 && (_buffer!!.length() == 1 || _buffer!!.bytes[1].toInt() == 1)) {
                    return connection.close()
                }

                /**
                 * PLAY
                 */

                if (_byteSize == -1) {
                    _byteSize = _buffer!!.readVarInt()

                    if (_byteSize == -1) {
                        return
                    }
                }

                do {
                    val size = _byteSize.getVarIntSize()

                    if (_buffer!!.length() < _byteSize + size) {
                        return
                    }

                    val payloadSize = _byteSize + size

                    if (connection.protocol == Protocol.GAME) {
                        // TODO later
                    } else {
                        val innerBuffer = _buffer!!.getBuffer(size, payloadSize)

                        _buffer = if (payloadSize == _buffer!!.length()) {
                            null
                        } else {
                            _buffer!!.getBuffer(payloadSize, _buffer!!.length())
                        }

                        handler(innerBuffer.byteBuf)

                        println("Current buffer length: ${innerBuffer.length()}")
                    }

                    _byteSize = if (_buffer != null && (connection.protocol != Protocol.GAME || !connection.isOnlineMode)) {
                        _buffer!!.readVarInt()
                    } else {
                        -1
                    }
                } while (_byteSize != -1 && _buffer!!.length() > 0)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun exceptionHandler(
        handler: Handler<Throwable>
    ) = null

    override fun handler(
        handler: Handler<Buffer>
    ) = null

    override fun pause() = null

    override fun resume() = null

    override fun fetch(
        amount: Long
    ) = null

    override fun endHandler(
        endHandler: Handler<Void>
    ) = null

    override fun setOutput(
        output: Handler<Buffer>
    ) = Unit

    override fun maxRecordSize(
        size: Int
    ) = null

    override fun delimitedMode(
        delim: String
    ) = Unit

    override fun delimitedMode(
        delim: Buffer
    ) = Unit

    override fun fixedSizeMode(
        size: Int
    ) = Unit

}
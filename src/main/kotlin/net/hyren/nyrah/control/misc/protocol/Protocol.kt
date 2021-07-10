package net.hyren.nyrah.control.misc.protocol

import net.hyren.core.shared.misc.kotlin.sizedArray
import net.hyren.nyrah.control.misc.protocol.packet.IPacket
import net.hyren.nyrah.control.misc.protocol.packet.implementations.*
import java.util.function.Supplier
import kotlin.reflect.KClass

/**
 * @author Gutyerrez
 */
enum class Protocol {

    HANDSHAKE {
        init {
            TO_SERVER.registerPacket(
                0x00,
                HandshakePacket::class
            ) { HandshakePacket() }
        }
    },
    STATUS {
        init {
            TO_SERVER.registerPacket(
                0x00,
                StatusRequestPacket::class
            ) { StatusRequestPacket() }
            TO_SERVER.registerPacket(
                0x01,
                PingPacket::class
            ) { PingPacket() }

            TO_CLIENT.registerPacket(
                0x00,
                StatusResponsePacket::class
            ) { StatusResponsePacket() }
            TO_CLIENT.registerPacket(
                0x01,
                PingPacket::class
            ) { PingPacket() }
        }
    },
    LOGIN {
        init {
            TO_SERVER.registerPacket(
                0x00,
                LoginStartPacket::class
            ) { LoginStartPacket() }
            TO_SERVER.registerPacket(
                0x01,
                EncryptionResponsePacket::class
            ) { EncryptionResponsePacket() }
            TO_SERVER.registerPacket(
                0x03,
                NyrahLoginRequestPacket::class
            ) { NyrahLoginRequestPacket() }

            TO_CLIENT.registerPacket(
                0x00,
                DisconnectPacket::class
            ) { DisconnectPacket() }
            TO_CLIENT.registerPacket(
                0x01,
                EncryptionRequestPacket::class
            ) { EncryptionRequestPacket() }
            TO_CLIENT.registerPacket(
                0x02,
                LoginSuccessPacket::class
            ) { LoginSuccessPacket() }
            TO_CLIENT.registerPacket(
                0x03,
                NetworkCompressionPacket::class
            ) { NetworkCompressionPacket() }
            TO_CLIENT.registerPacket(
                0x05,
                NyrahLoginResponsePacket::class
            ) { NyrahLoginResponsePacket() }
        }
    },
    GAME;

    val TO_SERVER = DirectionData(Direction.TO_SERVER)
    val TO_CLIENT = DirectionData(Direction.TO_CLIENT)

    enum class Direction {

        TO_CLIENT,
        TO_SERVER;

    }

    data class DirectionData(
        val direction: Direction
    ) {

        private val packetIds = mutableMapOf<Int, KClass<out IPacket>>()
        private val packetKClasses = mutableMapOf<KClass<out IPacket>, Int>()

        private val constructors = sizedArray<Supplier<out IPacket>>(0xFF)

        fun registerPacket(
            packetId: Int,
            kClass: KClass<out IPacket>,
            constructor: Supplier<out IPacket>
        ) {
            packetIds[packetId] = kClass
            packetKClasses[kClass] = packetId

            constructors[packetId] = constructor
        }

        fun createPacket(
            id: Int
        ): IPacket? = constructors.firstOrNull { constructors.indexOf(it) == id }?.get()

        fun getPacketId(
            packet: IPacket
        ) = packetKClasses[packet::class]

    }

}
package net.hyren.nyrah.control.misc.protocol

import kotlin.reflect.KClass
import net.hyren.core.shared.misc.kotlin.sizedArray
import net.hyren.nyrah.control.NyrahConstants
import net.hyren.nyrah.control.misc.protocol.packet.IPacket
import net.hyren.nyrah.control.misc.protocol.packet.implementations.HandshakePacket
import net.hyren.nyrah.control.misc.protocol.packet.implementations.PingPacket
import net.hyren.nyrah.control.misc.protocol.packet.implementations.StatusRequestPacket
import net.hyren.nyrah.control.misc.protocol.packet.implementations.StatusResponsePacket
import java.util.function.Supplier

/**
 * @author Gutyerrez
 */
enum class Protocol(
    val TO_SERVER: DirectionData,
    val TO_CLIENT: DirectionData
) {

    HANDSHAKE(
        DirectionData(Direction.TO_SERVER),
        DirectionData(Direction.TO_CLIENT)
    ) {

        init {
            TO_SERVER.registerPacket(
                0x00,
                HandshakePacket::class
            ) {HandshakePacket()}
        }
    },
    STATUS(
        DirectionData(Direction.TO_SERVER),
        DirectionData(Direction.TO_CLIENT)
    ) {
        init {
            TO_SERVER.registerPacket(
                0x00,
                StatusRequestPacket::class
            ) {StatusRequestPacket()}
            TO_SERVER.registerPacket(
                0x01,
                PingPacket::class
            ) {PingPacket()}

            TO_CLIENT.registerPacket(
                0x00,
                StatusResponsePacket::class
            ) {StatusResponsePacket()}
            TO_CLIENT.registerPacket(
                0x01,
                PingPacket::class
            ) {PingPacket()}
        }
    },
    GAME(
        DirectionData(Direction.TO_SERVER),
        DirectionData(Direction.TO_CLIENT)
    );

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
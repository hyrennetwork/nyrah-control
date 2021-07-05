package net.hyren.nyrah.control

import io.vertx.core.AbstractVerticle
import io.vertx.core.Vertx
import io.vertx.core.VertxOptions
import net.hyren.core.shared.CoreProvider
import net.hyren.nyrah.control.handler.implementations.InitialHandler

/**
 * @author Gutyerrez
 */
object NyrahControl {

    @JvmStatic
    fun main(args: Array<String>) {
        CoreProvider.prepare(25565)

        val server = object : AbstractVerticle() {
            override fun start() {
                vertx.createNetServer()
                    .exceptionHandler { throw it }
                    .connectHandler { InitialHandler(it) }
                    .listen(25565)
                    .onSuccess { println("Nyrah Control is running on ${it.actualPort()}") }
                    .onFailure { throw it }
            }

        }
        val vertx = Vertx.vertx(VertxOptions().apply {
            preferNativeTransport = true
        })

        vertx.deployVerticle(server)
    }

}
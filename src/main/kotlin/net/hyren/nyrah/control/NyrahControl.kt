package net.hyren.nyrah.control

import io.vertx.core.AbstractVerticle
import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.core.VertxOptions
import net.hyren.nyrah.control.handler.implementations.InitialHandler
import net.hyren.nyrah.control.misc.protocol.Protocol

/**
 * @author Gutyerrez
 */
object NyrahControl {

    @JvmStatic
    fun main(args: Array<String>) {
        val server = object : AbstractVerticle() {

            override fun start() {
                vertx.createNetServer()
                    .exceptionHandler { throw it }
                    .connectHandler { InitialHandler(it) }
                    .listen(25565)
                    .onSuccess { NyrahConstants.LOGGER.info("Nyrah Control is running on ${it.actualPort()}") }
                    .onFailure { throw it }
            }

        }

        val vertx = Vertx.vertx(VertxOptions().apply {
            preferNativeTransport = true
        })

        vertx.deployVerticle(server)
    }

}
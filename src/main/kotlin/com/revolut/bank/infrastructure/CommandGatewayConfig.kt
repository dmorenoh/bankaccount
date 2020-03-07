package com.revolut.bank.infrastructure

import io.quarkus.runtime.StartupEvent
import io.vertx.axle.core.eventbus.EventBus
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes


@ApplicationScoped
class CommandGatewayConfig (private val eventBus: EventBus) {

    fun onStart(@Observes ev: StartupEvent?) {
    }
}
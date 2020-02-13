package com.revolut.bank.infrastructure

import com.revolut.common.Event
import com.revolut.common.EventBus
import java.util.concurrent.CompletionStage
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class QuarkusEventBus(private val eventBus: io.vertx.axle.core.eventbus.EventBus) : EventBus {
    override fun send(event: Event): CompletionStage<Void> {
        return eventBus.request<Void>(event::class.simpleName, event)
                .thenApply { it.body() }
    }
}
package com.revolut.bank.infrastructure

import com.revolut.common.Command
import com.revolut.common.CommandBus
import java.util.concurrent.CompletionStage
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class QuarkusCommandBus(private val eventBus: io.vertx.axle.core.eventbus.EventBus) : CommandBus {
    override fun send(command: Command): CompletionStage<Void> {
        return eventBus.request<Void>(command::class.simpleName, command)
                .thenApply { it.body() }
    }
}
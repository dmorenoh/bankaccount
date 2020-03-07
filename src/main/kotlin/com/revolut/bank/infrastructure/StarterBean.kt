package com.revolut.bank.infrastructure

import com.revolut.bank.domain.account.impl.DefaultAccountService
import com.revolut.bank.infrastructure.repository.InMemoryAccountRepository
import io.quarkus.runtime.StartupEvent
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes


@ApplicationScoped
class StarterBean(private val eventBus: io.vertx.axle.core.eventbus.EventBus) {

    fun startup(@Observes event: StartupEvent, quarkusEventBus: QuarkusEventBus, quarkusCommandBus: QuarkusCommandBus) {
        val accountService = DefaultAccountService(InMemoryAccountRepository(), quarkusEventBus)
    }
}
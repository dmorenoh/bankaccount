package com.revolut.bank.domain.moneytransfer

import com.revolut.common.EventBus
import com.revolut.bank.domain.moneytransfer.MoneyTransfer.Companion.newMoneyTransfer
import io.quarkus.vertx.ConsumeEvent
import mu.KotlinLogging
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class MoneyTransferService(private val moneyTransferRepository: MoneyTransferRepository,
                           private val eventBus: EventBus) {

    private val logger = KotlinLogging.logger {}

    @ConsumeEvent("RequestMoneyTransferCommand")
    fun handle(command: RequestMoneyTransferCommand) {

        val moneyTransfer = newMoneyTransfer(command)

        moneyTransferRepository.add(moneyTransfer)

        eventBus.send(MoneyTransferRequestedEvent.of(moneyTransfer))
    }

    @ConsumeEvent("CompleteMoneyTransferCommand")
    fun handle(command: CompleteMoneyTransferCommand) {
        logger.info { "Money transfer completed:$command" }
        moneyTransferRepository.get(command.transferId)
                ?.let { moneyTransfer ->
                    moneyTransfer.complete()
                    eventBus.send(MoneyTransferCompletedEvent.of(moneyTransfer))
                }
    }

    @ConsumeEvent("CancelMoneyTransferCommand")
    fun handle(command: CancelMoneyTransferCommand) {
        logger.info { "Money transfer cancelled:$command" }
        moneyTransferRepository.get(command.transferId)
                ?.let { moneyTransfer ->
                    moneyTransfer.cancel()
                    eventBus.send(MoneyTransferCancelledEvent.of(moneyTransfer))
                }
    }
}
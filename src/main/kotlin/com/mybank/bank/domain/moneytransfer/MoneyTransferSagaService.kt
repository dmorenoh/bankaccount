package com.mybank.bank.domain.moneytransfer

import com.mybank.bank.domain.account.DepositMoneyCommand
import com.mybank.bank.domain.account.MoneyDepositedEvent
import com.mybank.bank.domain.account.MoneyWithdrawnEvent
import com.mybank.bank.domain.account.WithdrawMoneyCommand
import com.mybank.common.CommandBus
import io.quarkus.vertx.ConsumeEvent
import mu.KotlinLogging
import javax.inject.Singleton

@Singleton
class MoneyTransferSagaService(private val repository: MoneyTransferRepository,
                               private val commandBus: CommandBus) {

    private val logger = KotlinLogging.logger {}

    @ConsumeEvent("MoneyTransferRequestedEvent")
    fun on(event: MoneyTransferRequestedEvent) {
        logger.info { "Withdraw money transfer :$event" }
        commandBus.send(WithdrawMoneyCommand(event.from, event.transferId, event.amount))
                .exceptionally {
                    commandBus.send(CancelMoneyTransferCommand(event.transferId))
                    return@exceptionally null
                }
    }

    @ConsumeEvent("MoneyWithdrawnEvent")
    fun on(event: MoneyWithdrawnEvent) {
        logger.info { "Deposit money transfer :$event" }
        repository.get(event.transactionId)?.let {
            commandBus.send(DepositMoneyCommand(
                    it.targetAccountNumber(),
                    it.transferId.toString(),
                    it.amount))
        }
    }

    @ConsumeEvent("MoneyDepositedEvent")
    fun on(event: MoneyDepositedEvent) {
        commandBus.send(CompleteMoneyTransferCommand(event.transactionId))
    }

    @ConsumeEvent("MoneyTransferCompletedEvent")
    fun on(event: MoneyTransferCompletedEvent) {

    }
}
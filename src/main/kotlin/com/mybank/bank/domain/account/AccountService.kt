package com.mybank.bank.domain.account

import com.mybank.common.EventBus
import io.quarkus.vertx.ConsumeEvent
import mu.KotlinLogging
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class AccountService(private val repository: AccountRepository,
                     private val eventBus: EventBus) {
    private val logger = KotlinLogging.logger {}

    @ConsumeEvent("CreateAccountCommand")
    fun handle(command: CreateAccountCommand) {
        logger.info { "Start CreateAccountCommand" }
        repository.add(Account(command.accountNumber, command.currentBalance))
        eventBus.send(AccountCreatedEvent(command.accountNumber, command.currentBalance))
    }

    @ConsumeEvent("WithdrawMoneyCommand")
    fun handle(command: WithdrawMoneyCommand) {
        logger.info { "Start WithdrawMoneyCommand:$command" }
        repository.get(command.accountNumber)?.let { account ->
            account.withdraw(command.amount)
            eventBus.send(MoneyWithdrawnEvent(command.transactionId, account.accountNumber, command.amount))
        }
    }

    @ConsumeEvent("DepositMoneyCommand")
    fun handle(command: DepositMoneyCommand) {
        repository.get(command.accountNumber)?.let { account ->
            account.deposit(command.amount)
            eventBus.send(MoneyDepositedEvent(command.transactionId, account.accountNumber, command.amount))
        }
    }

}






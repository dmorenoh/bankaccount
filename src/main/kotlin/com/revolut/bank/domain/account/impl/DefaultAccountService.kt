package com.revolut.bank.domain.account.impl

import com.revolut.bank.domain.account.*
import com.revolut.common.EventBus
import mu.KotlinLogging

class DefaultAccountService(private val repository: AccountRepository,
                            private val eventBus: EventBus) : AccountService {
    private val logger = KotlinLogging.logger {}

    override fun handle(command: CreateAccountCommand) {
        logger.info { "Start CreateAccountCommand" }
        repository.add(Account(command.accountNumber, command.currentBalance))
        eventBus.send(AccountCreatedEvent(command.accountNumber, command.currentBalance))
    }

    override fun handle(command: WithdrawMoneyCommand) {
        logger.info { "Start WithdrawMoneyCommand:$command" }
        repository.get(command.accountNumber)?.let { account ->
            account.withdraw(command.amount)
            eventBus.send(MoneyWithdrawnEvent(command.transactionId, account.accountNumber, command.amount))
        }
    }

    override fun handle(command: DepositMoneyCommand) {
        repository.get(command.accountNumber)?.let { account ->
            account.deposit(command.amount)
            eventBus.send(MoneyDepositedEvent(command.transactionId, account.accountNumber, command.amount))
        }
    }
}
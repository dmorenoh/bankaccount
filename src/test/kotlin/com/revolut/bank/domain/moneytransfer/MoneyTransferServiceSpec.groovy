package com.revolut.bank.domain.moneytransfer

import com.revolut.bank.domain.account.Account
import com.revolut.bank.domain.account.AccountNumber
import com.revolut.common.EventBus
import com.revolut.common.values.Money
import spock.lang.Specification

class MoneyTransferServiceSpec extends Specification {


    private static final Money SOME_MONEY = new Money(BigDecimal.TEN, Currency.getInstance('EUR'))
    private static final AccountNumber EXISTING_ACCOUNT_NUMBER = new AccountNumber('existingAccount')
    private static final AccountNumber EXISTING_ACCOUNT_NUMBER_2 = new AccountNumber('existingAccount2')
    private static final Account ACCOUNT_SOURCE = new Account(EXISTING_ACCOUNT_NUMBER, SOME_MONEY)
    private static final Account ACCOUNT_TARGET = new Account(EXISTING_ACCOUNT_NUMBER_2, SOME_MONEY)
    private static final UUID ANY_TRANSACTION_ID = UUID.randomUUID()

    MoneyTransferRepository moneyTransferRepository = Mock(MoneyTransferRepository.class)
    EventBus eventBus = Mock(EventBus.class)

    def subject = new MoneyTransferService(moneyTransferRepository, eventBus)


    def "should request money transfer when valid command"() {
        when: "request transfer"
            subject.handle(new RequestMoneyTransferCommand(ACCOUNT_SOURCE, ACCOUNT_TARGET, SOME_MONEY))
        then: "accept request"
            1 * moneyTransferRepository.add({
                MoneyTransfer moneyTransfer ->
                    moneyTransfer.from == ACCOUNT_SOURCE
                    moneyTransfer.to == ACCOUNT_TARGET
                    moneyTransfer.amount == SOME_MONEY
                    moneyTransfer.status == MoneyTransferStatus.REQUESTED
            })
            1 * eventBus.send(_ as MoneyTransferRequestedEvent)
    }

    def "should complete money transfer when exist"() {
        given: "an existing money transfer"
            def moneyTransfer = new MoneyTransfer(ANY_TRANSACTION_ID,
                    ACCOUNT_SOURCE, ACCOUNT_TARGET, SOME_MONEY, MoneyTransferStatus.IN_PROGRESS)
            moneyTransferRepository.get(ANY_TRANSACTION_ID.toString()) >> moneyTransfer
        when: "request to complete"
            subject.handle(new CompleteMoneyTransferCommand(ANY_TRANSACTION_ID.toString()))
        then: "complete money transfer"
            moneyTransfer.status == MoneyTransferStatus.COMPLETED
    }

    def "should cancel money transfer when exist"() {
        given: "an existing money transfer"
            def moneyTransfer = new MoneyTransfer(ANY_TRANSACTION_ID,
                    ACCOUNT_SOURCE, ACCOUNT_TARGET, SOME_MONEY, MoneyTransferStatus.IN_PROGRESS)
            moneyTransferRepository.get(ANY_TRANSACTION_ID.toString()) >> moneyTransfer
        when: "request to cancel"
            subject.handle(new CancelMoneyTransferCommand(ANY_TRANSACTION_ID.toString()))
        then: "cancel money transfer"
            moneyTransfer.status == MoneyTransferStatus.CANCELED
    }
}

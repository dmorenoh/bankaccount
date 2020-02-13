package com.revolut.bank.domain.account


import com.revolut.common.EventBus
import com.revolut.common.values.Money
import spock.lang.Specification

class AccountServiceSpec extends Specification {

    private static final AccountNumber ACCOUNT_NUMBER = new AccountNumber('1234')
    private static final Money NO_MONEY = new Money(BigDecimal.ZERO, Currency.getInstance('EUR'))
    private static final Money SOME_MONEY = new Money(BigDecimal.TEN, Currency.getInstance('EUR'))
    private static final String TRANSACTION_ID = '123'

    AccountRepository repository = Mock(AccountRepository.class)

    EventBus eventBus = Mock(EventBus.class)

    AccountService accountService = new AccountService(repository, eventBus)

    def 'should fail when not enough balance'() {
        given: "an account with no money"
            repository.get(ACCOUNT_NUMBER) >> new Account(ACCOUNT_NUMBER, NO_MONEY)
        when: "withdraw money from this account"
            accountService.handle(new WithdrawMoneyCommand(ACCOUNT_NUMBER, TRANSACTION_ID, SOME_MONEY))
        then: "fails"
            thrown WithdrawException
            0 * eventBus.send(_)
    }

    def 'should do nothing when account not found'() {
        given: "an non-existing account"
            repository.get(ACCOUNT_NUMBER) >> null
        when: "withdraw money from this account"
            accountService.handle(new WithdrawMoneyCommand(ACCOUNT_NUMBER, TRANSACTION_ID, SOME_MONEY))
        then: "do nothing"
            0 * eventBus.send(_)
    }

    def 'should withdraw money when enough balance'() {
        given: "an account with money"
            def account = new Account(ACCOUNT_NUMBER, SOME_MONEY)
            repository.get(ACCOUNT_NUMBER) >> account
        when: "withdraw money from this account"
            accountService.handle(new WithdrawMoneyCommand(ACCOUNT_NUMBER, TRANSACTION_ID, SOME_MONEY))
        then: "balance updated"
            account.balance.value == BigDecimal.ZERO
            1 * eventBus.send(new MoneyWithdrawnEvent(TRANSACTION_ID, ACCOUNT_NUMBER, SOME_MONEY))
    }

    def "should do nothing when trying to deposit money to non-existing account"() {
        given: "an non-existing account"
            repository.get(ACCOUNT_NUMBER) >> null
        when: "deposit money from this account"
            accountService.handle(new DepositMoneyCommand(ACCOUNT_NUMBER, TRANSACTION_ID, SOME_MONEY))
        then: "do nothing"
            0 * eventBus.send(_)
    }

    def "should deposit money when valid account"() {
        given: "an account without money"
            def account = new Account(ACCOUNT_NUMBER, NO_MONEY)
            repository.get(ACCOUNT_NUMBER) >> account
        when: "deposit money from this account"
            accountService.handle(new DepositMoneyCommand(ACCOUNT_NUMBER, TRANSACTION_ID, SOME_MONEY))
        then: "balance updated"
            account.balance == SOME_MONEY
            1 * eventBus.send(new MoneyDepositedEvent(TRANSACTION_ID, ACCOUNT_NUMBER, SOME_MONEY))

    }
}

package com.mybank.bank.domain.moneytransfer

import com.mybank.bank.domain.account.Account
import com.mybank.bank.domain.account.AccountNumber
import com.mybank.bank.infrastructure.repository.InMemoryAccountRepository
import com.mybank.bank.infrastructure.repository.InMemoryMoneyTransferRepository
import com.mybank.common.values.Money
import io.quarkus.test.junit.QuarkusTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.*
import javax.enterprise.inject.Default
import javax.inject.Inject

const val ACCOUNT_NUMBER_1 = "1111"
const val ACCOUNT_NUMBER_2 = "2222"

@QuarkusTest
internal class MoneyTransferSagaServiceTest {

    @Inject
    @field: Default
    lateinit var moneyTransferSagaService: MoneyTransferSagaService

    companion object {
        private var TEN_EUROS = Money(BigDecimal.TEN, Currency.getInstance("EUR"))
        private var ZERO_EUROS = Money(BigDecimal.ZERO, Currency.getInstance("EUR"))
    }

    @Before
    fun setup() {
        InMemoryAccountRepository.accounts.clear()
        InMemoryMoneyTransferRepository.moneyTransfers.clear()
    }

    @Test
    fun shouldCancelMoneyTransferWhenSourceAccountHaveNoEnoughMoney() {
        //Given an existing money transfer
        val source = Account(AccountNumber(ACCOUNT_NUMBER_1), ZERO_EUROS)
        val target = Account(AccountNumber(ACCOUNT_NUMBER_2), TEN_EUROS)
        val requestedMoneyTransfer = MoneyTransfer(UUID.randomUUID(), source, target, TEN_EUROS, MoneyTransferStatus.REQUESTED)
        InMemoryAccountRepository.accounts = hashMapOf(
                source.accountNumber to source,
                target.accountNumber to target)
        InMemoryMoneyTransferRepository.moneyTransfers = hashMapOf(
                requestedMoneyTransfer.transferId.toString() to requestedMoneyTransfer)

        //When requested to transfer
        moneyTransferSagaService.on(MoneyTransferRequestedEvent(
                requestedMoneyTransfer.transferId.toString(), requestedMoneyTransfer.sourceAccountNumber(),
                requestedMoneyTransfer.targetAccountNumber(), requestedMoneyTransfer.amount))


        //Then money transfer canceled and accounts keep the initial balance
        Thread.sleep(100)
        assertEquals(MoneyTransferStatus.CANCELED, requestedMoneyTransfer.currentStatus())
        assertEquals(source.currentBalance(), ZERO_EUROS)
        assertEquals(target.currentBalance(), TEN_EUROS)
    }

    @Test
    fun shouldCompleteMoneyTransferWhenSourceAccountHaveEnoughMoney() {
        //Given an existing money transfer
        val source = Account(AccountNumber(ACCOUNT_NUMBER_1), TEN_EUROS)
        val target = Account(AccountNumber(ACCOUNT_NUMBER_2), ZERO_EUROS)
        val requestedMoneyTransfer = MoneyTransfer(UUID.randomUUID(), source, target, TEN_EUROS, MoneyTransferStatus.REQUESTED)

        InMemoryAccountRepository.accounts = hashMapOf(
                source.accountNumber to source,
                target.accountNumber to target)


        InMemoryMoneyTransferRepository.moneyTransfers = hashMapOf(
                requestedMoneyTransfer.transferId.toString() to requestedMoneyTransfer)

        //When requested to transfer
        moneyTransferSagaService.on(MoneyTransferRequestedEvent(
                requestedMoneyTransfer.transferId.toString(), requestedMoneyTransfer.sourceAccountNumber(),
                requestedMoneyTransfer.targetAccountNumber(), requestedMoneyTransfer.amount))


        //Then money transfer canceled and accounts keep the initial balance
        Thread.sleep(100)
        assertEquals(MoneyTransferStatus.COMPLETED, requestedMoneyTransfer.currentStatus())
        assertEquals(source.currentBalance(), ZERO_EUROS)
        assertEquals(target.currentBalance(), TEN_EUROS)
    }

    @Test
    fun shouldProcessFirstTransactionAndCancelSecondWhenConcurrent() {
        //Given an existing money transfer
        val source = Account(AccountNumber(ACCOUNT_NUMBER_1), TEN_EUROS)
        val target = Account(AccountNumber(ACCOUNT_NUMBER_2), ZERO_EUROS)
        val requestedMoneyTransfer = MoneyTransfer(UUID.randomUUID(), source, target, TEN_EUROS, MoneyTransferStatus.REQUESTED)
        val requestedMoneyTransfer2 = MoneyTransfer(UUID.randomUUID(), source, target, TEN_EUROS, MoneyTransferStatus.REQUESTED)

        InMemoryAccountRepository.accounts = hashMapOf(
                source.accountNumber to source,
                target.accountNumber to target)

        InMemoryMoneyTransferRepository.moneyTransfers = hashMapOf(
                requestedMoneyTransfer.transferId.toString() to requestedMoneyTransfer,
                requestedMoneyTransfer2.transferId.toString() to requestedMoneyTransfer2)

        //When requested to transfer
        moneyTransferSagaService.on(MoneyTransferRequestedEvent(
                requestedMoneyTransfer.transferId.toString(), requestedMoneyTransfer.sourceAccountNumber(),
                requestedMoneyTransfer.targetAccountNumber(), requestedMoneyTransfer.amount))
        moneyTransferSagaService.on(MoneyTransferRequestedEvent(
                requestedMoneyTransfer2.transferId.toString(), requestedMoneyTransfer2.sourceAccountNumber(),
                requestedMoneyTransfer2.targetAccountNumber(), requestedMoneyTransfer2.amount))

        //Then money transfer canceled and accounts keep the initial balance
        Thread.sleep(500)
        assertEquals(MoneyTransferStatus.COMPLETED, requestedMoneyTransfer.currentStatus())
        assertEquals(source.currentBalance(), ZERO_EUROS)
        assertEquals(target.currentBalance(), TEN_EUROS)

        assertEquals(MoneyTransferStatus.CANCELED, requestedMoneyTransfer2.currentStatus())
        assertEquals(source.currentBalance(), ZERO_EUROS)
        assertEquals(target.currentBalance(), TEN_EUROS)
    }
}
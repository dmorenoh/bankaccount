package com.revolut.bank.domain.moneytransfer

import com.revolut.bank.domain.account.AccountNumber
import com.revolut.common.Event
import com.revolut.common.values.Money

data class MoneyTransferRequestedEvent(val transferId: String,
                                       val from: AccountNumber,
                                       val to: AccountNumber,
                                       val amount: Money) : Event {
    companion object {
        fun of(moneyTransfer: MoneyTransfer): MoneyTransferRequestedEvent =
                MoneyTransferRequestedEvent(moneyTransfer.transferId.toString(),
                        moneyTransfer.sourceAccountNumber(),
                        moneyTransfer.targetAccountNumber(),
                        moneyTransfer.amount)
    }
}

data class MoneyTransferCancelledEvent(val transferId: String) : Event {
    companion object {
        fun of(moneyTransfer: MoneyTransfer): MoneyTransferCancelledEvent =
                MoneyTransferCancelledEvent(moneyTransfer.transferId.toString())
    }
}

data class MoneyTransferCompletedEvent(val transferId: String) : Event {
    companion object {
        fun of(moneyTransfer: MoneyTransfer): MoneyTransferCompletedEvent =
                MoneyTransferCompletedEvent(moneyTransfer.transferId.toString())
    }
}
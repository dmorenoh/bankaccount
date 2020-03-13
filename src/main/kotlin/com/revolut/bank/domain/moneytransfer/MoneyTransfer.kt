package com.revolut.bank.domain.moneytransfer

import com.revolut.bank.domain.account.Account
import com.revolut.common.values.Money
import java.util.*

data class MoneyTransfer(val transferId: UUID,
                         val from: Account,
                         val to: Account,
                         val amount: Money,
                         private var status: MoneyTransferStatus) {

    fun sourceAccountNumber() = this.from.accountNumber

    fun targetAccountNumber() = this.to.accountNumber

    fun cancel() {
        this.status = MoneyTransferStatus.CANCELED
    }

    fun complete() {
        this.status = MoneyTransferStatus.COMPLETED
    }

    companion object {
        fun newMoneyTransfer(command: RequestMoneyTransferCommand): MoneyTransfer =
                MoneyTransfer(UUID.randomUUID(),
                        command.source,
                        command.target,
                        command.amount,
                        MoneyTransferStatus.REQUESTED)
    }

    fun currentStatus(): MoneyTransferStatus = status
}
package com.revolut.bank.infrastructure.repository

import com.revolut.bank.domain.moneytransfer.MoneyTransfer
import com.revolut.bank.domain.moneytransfer.MoneyTransferRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class InMemoryMoneyTransferRepository : MoneyTransferRepository {
    override fun add(moneyTransfer: MoneyTransfer) {
        moneyTransfers[moneyTransfer.transferId.toString()] = moneyTransfer
    }

    override fun get(transferId: String): MoneyTransfer? = moneyTransfers[transferId]

    override fun getAll(): List<MoneyTransfer> = moneyTransfers.values.toList()

    companion object {
        var moneyTransfers = HashMap<String, MoneyTransfer>()
    }
}
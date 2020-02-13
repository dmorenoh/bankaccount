package com.revolut.bank.domain.moneytransfer

interface MoneyTransferRepository {
    fun add(moneyTransfer: MoneyTransfer)
    fun get(transferId: String): MoneyTransfer?
    fun getAll():List<MoneyTransfer>
}
package com.mybank.bank.domain.account

import com.mybank.common.values.Money

data class Account(val accountNumber: AccountNumber,
                   private var balance: Money) {
    constructor(command: CreateAccountCommand) : this(command.accountNumber, command.currentBalance)

    fun deposit(amount: Money) {
        balance = balance.add(amount)
    }

    fun withdraw(amount: Money) {
        if (balance < amount)
            throw WithdrawException("non valid amount")
        balance = balance.subtract(amount)
    }

    fun currentBalance():Money = balance
}
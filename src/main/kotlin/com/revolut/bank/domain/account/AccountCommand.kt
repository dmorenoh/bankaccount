package com.revolut.bank.domain.account

import com.revolut.common.Command
import com.revolut.common.values.Money

data class CreateAccountCommand(
        val accountNumber: AccountNumber,
        val currentBalance: Money):Command

data class WithdrawMoneyCommand(
        val accountNumber: AccountNumber,
        val transactionId: String,
        val amount: Money) : Command

data class DepositMoneyCommand(
        val accountNumber: AccountNumber,
        val transactionId: String,
        val amount: Money) : Command

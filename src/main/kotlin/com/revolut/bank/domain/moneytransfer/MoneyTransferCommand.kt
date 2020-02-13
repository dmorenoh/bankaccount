package com.revolut.bank.domain.moneytransfer

import com.revolut.bank.domain.account.Account
import com.revolut.common.Command
import com.revolut.common.values.Money

data class RequestMoneyTransferCommand(val source: Account, val target: Account, val amount: Money) : Command
data class CancelMoneyTransferCommand(val transferId: String) : Command
data class CompleteMoneyTransferCommand(val transferId: String) : Command
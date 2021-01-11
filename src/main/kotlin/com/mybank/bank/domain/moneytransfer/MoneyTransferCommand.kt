package com.mybank.bank.domain.moneytransfer

import com.mybank.bank.domain.account.Account
import com.mybank.common.Command
import com.mybank.common.values.Money

data class RequestMoneyTransferCommand(val source: Account, val target: Account, val amount: Money) : Command
data class CancelMoneyTransferCommand(val transferId: String) : Command
data class CompleteMoneyTransferCommand(val transferId: String) : Command
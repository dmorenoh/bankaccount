package com.mybank.bank.domain.account

import com.mybank.common.Event
import com.mybank.common.values.Money

data class AccountCreatedEvent(val account: AccountNumber, val balance: Money) : Event
data class MoneyWithdrawnEvent(val transactionId: String, val account: AccountNumber, val money: Money) : Event
data class MoneyDepositedEvent(val transactionId: String, val account: AccountNumber, val money: Money) : Event
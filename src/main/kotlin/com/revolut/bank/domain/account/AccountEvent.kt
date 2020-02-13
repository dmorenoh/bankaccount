package com.revolut.bank.domain.account

import com.revolut.common.Event
import com.revolut.common.values.Money

data class AccountCreatedEvent(val account: AccountNumber, val balance: Money) : Event
data class MoneyWithdrawnEvent(val transactionId: String, val account: AccountNumber, val money: Money) : Event
data class MoneyDepositedEvent(val transactionId: String, val account: AccountNumber, val money: Money) : Event
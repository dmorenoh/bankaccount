package com.mybank.bank.rest.message

import com.mybank.bank.domain.account.Account
import java.math.BigDecimal
import javax.json.bind.annotation.JsonbCreator

data class AccountResponse @JsonbCreator constructor(
        val accountNumber: String,
        val initialBalance: BigDecimal) {

    companion object {
        fun from(account: Account): AccountResponse =
                AccountResponse(
                        account.accountNumber.value,
                        account.currentBalance().value)
    }
}
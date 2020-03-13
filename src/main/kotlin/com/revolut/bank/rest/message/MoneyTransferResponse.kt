package com.revolut.bank.rest.message

import com.revolut.bank.domain.moneytransfer.MoneyTransfer
import java.math.BigDecimal
import javax.json.bind.annotation.JsonbCreator

data class MoneyTransferResponse @JsonbCreator constructor(
        val moneyTransferId: String,
        val sourceAccountNumber: String,
        val targetAccountNumber: String,
        val amount: BigDecimal,
        val status: String) {
    companion object {
        fun from(moneyTransfer: MoneyTransfer): MoneyTransferResponse =
                MoneyTransferResponse(
                        moneyTransfer.transferId.toString(),
                        moneyTransfer.sourceAccountNumber().value,
                        moneyTransfer.targetAccountNumber().value,
                        moneyTransfer.amount.value,
                        moneyTransfer.currentStatus().name)
    }
}
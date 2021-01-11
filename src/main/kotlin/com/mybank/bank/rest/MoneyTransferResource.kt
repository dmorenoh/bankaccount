package com.mybank.bank.rest

import com.mybank.bank.domain.account.Account
import com.mybank.bank.domain.account.AccountNumber
import com.mybank.bank.domain.account.AccountRepository
import com.mybank.common.CommandBus
import com.mybank.common.values.Money
import com.mybank.bank.domain.moneytransfer.MoneyTransferException
import com.mybank.bank.domain.moneytransfer.MoneyTransferRepository
import com.mybank.bank.domain.moneytransfer.RequestMoneyTransferCommand
import com.mybank.bank.rest.message.MoneyTransferRequest
import com.mybank.bank.rest.message.MoneyTransferResponse
import java.util.*
import javax.validation.Valid
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/moneyTransfer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class MoneyTransferResource(val accountRepository: AccountRepository,
                            val moneyTransferRepository: MoneyTransferRepository,
                            val commandBus: CommandBus) {

    @POST
    fun requestMoneyTransfer(@Valid request: MoneyTransferRequest) {
        commandBus.send(RequestMoneyTransferCommand(
                getAccount(request.sourceAccountNumber),
                getAccount(request.targetAccountNumber),
                Money(request.amount, Currency.getInstance("EUR"))))
    }

    @GET
    @Path("/all")
    fun getTransactions(): List<MoneyTransferResponse> =
            moneyTransferRepository.getAll()
                    .map { moneyTransfer -> MoneyTransferResponse.from(moneyTransfer) }


    private fun getAccount(accountNumber: String): Account =
            accountRepository.get(AccountNumber(accountNumber))
                    ?: throw MoneyTransferException("Account number $accountNumber not found")

}
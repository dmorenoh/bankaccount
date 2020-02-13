package com.revolut.bank.rest

import com.revolut.bank.domain.account.Account
import com.revolut.bank.domain.account.AccountNumber
import com.revolut.bank.domain.account.AccountRepository
import com.revolut.bank.domain.moneytransfer.MoneyTransfer
import com.revolut.common.CommandBus
import com.revolut.common.values.Money
import com.revolut.bank.domain.moneytransfer.MoneyTransferException
import com.revolut.bank.domain.moneytransfer.MoneyTransferRepository
import com.revolut.bank.domain.moneytransfer.RequestMoneyTransferCommand
import com.revolut.bank.rest.message.MoneyTransferRequest
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
    fun getTransactions(): List<MoneyTransfer> = moneyTransferRepository.getAll()


    private fun getAccount(accountNumber: String): Account =
            accountRepository.get(AccountNumber(accountNumber))
                    ?: throw MoneyTransferException("Account number $accountNumber not found")

}
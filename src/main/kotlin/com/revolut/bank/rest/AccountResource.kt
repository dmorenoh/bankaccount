package com.revolut.bank.rest

import com.revolut.bank.domain.account.Account
import com.revolut.bank.domain.account.AccountNumber
import com.revolut.bank.domain.account.AccountRepository
import com.revolut.bank.domain.account.CreateAccountCommand
import com.revolut.common.CommandBus
import com.revolut.common.values.Money
import com.revolut.bank.rest.message.AccountRequest
import com.revolut.bank.rest.message.AccountResponse
import java.util.*
import javax.validation.Valid
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class AccountResource(val accountRepository: AccountRepository,
                      val commandBus: CommandBus) {

    @POST
    fun createAccount(@Valid accountMessage: AccountRequest) {
        val command = CreateAccountCommand(
                AccountNumber(accountMessage.accountNumber),
                Money(accountMessage.initialBalance,
                        Currency.getInstance("EUR")))
        commandBus.send(command)
    }

    @GET
    @Path("/all")
    fun getAll(): List<AccountResponse> =
            accountRepository.getAll()
                    .map { account -> AccountResponse.from(account) }

}


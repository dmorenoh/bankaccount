package com.mybank.bank.rest

import com.mybank.bank.domain.account.AccountNumber
import com.mybank.bank.domain.account.AccountRepository
import com.mybank.bank.domain.account.CreateAccountCommand
import com.mybank.common.CommandBus
import com.mybank.common.values.Money
import com.mybank.bank.rest.message.AccountRequest
import com.mybank.bank.rest.message.AccountResponse
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


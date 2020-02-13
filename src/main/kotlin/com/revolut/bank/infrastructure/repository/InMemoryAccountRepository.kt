package com.revolut.bank.infrastructure.repository

import com.revolut.bank.domain.account.Account
import com.revolut.bank.domain.account.AccountNumber
import com.revolut.bank.domain.account.AccountRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class InMemoryAccountRepository : AccountRepository {
    override fun add(account: Account) {
        accounts[account.accountNumber] = account
    }

    override fun get(id: AccountNumber): Account? = accounts[id]

    override fun getAll(): List<Account> = accounts.values.toList()

    companion object {
        var accounts = HashMap<AccountNumber, Account>()
    }
}
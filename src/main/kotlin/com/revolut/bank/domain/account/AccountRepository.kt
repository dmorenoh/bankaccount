package com.revolut.bank.domain.account

interface AccountRepository {
    fun add(account: Account)
    fun get(id: AccountNumber): Account?
    fun getAll():List<Account>
}
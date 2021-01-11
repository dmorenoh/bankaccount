package com.mybank.bank.rest

import com.mybank.bank.domain.account.Account
import com.mybank.bank.domain.account.AccountNumber
import com.mybank.common.values.Money
import com.mybank.bank.infrastructure.repository.InMemoryAccountRepository
import com.mybank.bank.infrastructure.repository.InMemoryMoneyTransferRepository
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.junit.Assert
import org.junit.Before
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.*

@QuarkusTest
class MoneyTransferResourceTest {

    companion object {
        private const val TARGET_ACCOUNT_NUMBER = "2222"
        private const val SOURCE_ACCOUNT_NUMBER = "1111"
    }

    @Before
    fun setup() {
        InMemoryAccountRepository.accounts.clear()
        InMemoryMoneyTransferRepository.moneyTransfers.clear()
    }

    @Test
    fun shouldFailWhenMoneyAmountIsNotAllowed() {

        given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "        \"targetAccountNumber\":\"2222\",\n" +
                        "        \"sourceAccountNumber\":\"1111\",\n" +
                        "        \"amount\":0.00\n" +
                        "}")
                .`when`().post("/moneyTransfer")
                .then().statusCode(400)
    }

    @Test
    fun shouldFailWhenSourceAccountIsBlank() {

        given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "        \"targetAccountNumber\":\"$TARGET_ACCOUNT_NUMBER\",\n" +
                        "        \"sourceAccountNumber\":\"\",\n" +
                        "        \"amount\":110.00\n" +
                        "}")
                .`when`().post("/moneyTransfer")
                .then().statusCode(400)
    }

    @Test
    fun shouldFailWhenTargetAccountIsBlank() {

        given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "        \"targetAccountNumber\":\"\",\n" +
                        "        \"sourceAccountNumber\":\"$SOURCE_ACCOUNT_NUMBER\",\n" +
                        "        \"amount\":0.00\n" +
                        "}")
                .`when`().post("/moneyTransfer")
                .then().statusCode(400)
    }


    @Test
    fun shouldFailWhenAccountDoesNotExist() {

        given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "        \"targetAccountNumber\":\"111\",\n" +
                        "        \"sourceAccountNumber\":\"222\",\n" +
                        "        \"amount\":100.00\n" +
                        "}")
                .`when`().post("/moneyTransfer")
                .then().statusCode(404)
    }

    @Test
    fun shouldRequestTransactionWhenAccountsExist() {

        val target = Account(AccountNumber(TARGET_ACCOUNT_NUMBER), Money(BigDecimal.TEN, Currency.getInstance("EUR")))
        val source = Account(AccountNumber(SOURCE_ACCOUNT_NUMBER), Money(BigDecimal.TEN, Currency.getInstance("EUR")))

        InMemoryAccountRepository.accounts = hashMapOf(source.accountNumber to source, target.accountNumber to target)

        given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "        \"targetAccountNumber\":\"$TARGET_ACCOUNT_NUMBER\",\n" +
                        "        \"sourceAccountNumber\":\"$SOURCE_ACCOUNT_NUMBER\",\n" +
                        "        \"amount\":${BigDecimal.TEN}\n" +
                        "}")
                .`when`().post("/moneyTransfer")
                .then().statusCode(204)
        Thread.sleep(500)
        Assert.assertEquals(BigDecimal("20"), target.currentBalance().value)
        Assert.assertEquals(BigDecimal("0"), source.currentBalance().value)
    }
}
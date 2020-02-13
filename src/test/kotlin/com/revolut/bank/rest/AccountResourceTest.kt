package com.revolut.bank.rest

import com.revolut.bank.infrastructure.repository.InMemoryAccountRepository
import com.revolut.bank.infrastructure.repository.InMemoryMoneyTransferRepository
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.junit.Before
import org.junit.jupiter.api.Test

@QuarkusTest
internal class AccountResourceTest {

    @Before
    fun setup() {
        InMemoryAccountRepository.accounts.clear()
        InMemoryMoneyTransferRepository.moneyTransfers.clear()
    }

    @Test
    fun shouldProcessCommandCreationWhenValidRequest() {
        given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "           \"accountNumber\":\"3333\",\n" +
                        "        \"initialBalance\":100.00\n" +
                        "}"
                )
                .`when`()
                .post("/account")
                .then()
                .statusCode(204)
    }

    @Test
    fun shouldFailWhenAccountNumberNotProvided() {
        given().contentType(ContentType.JSON)
                .body("{\n" +
                        "        \"accountNumber\":\"\",\n" +
                        "        \"initialBalance\":100.00\n" +
                        "}"
                )
                .`when`()
                .post("/account")
                .then()
                .statusCode(400)
    }

    @Test
    fun shouldFailWhenInitialBalanceIsNotAllowed() {
        given().contentType(ContentType.JSON)
                .body("{\n" +
                        "        \"accountNumber\":\"\",\n" +
                        "        \"initialBalance\":0.0\n" +
                        "}"
                )
                .`when`()
                .post("/account")
                .then()
                .statusCode(400)
    }
}
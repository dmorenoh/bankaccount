package com.revolut.bank.rest.message

import java.math.BigDecimal
import javax.json.bind.annotation.JsonbCreator
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.NotBlank

data class MoneyTransferRequest @JsonbCreator constructor(
        @get:NotBlank(message = "Source account number cannot be blank")
        val sourceAccountNumber: String,
        @get:NotBlank(message = "Source account number cannot be blank")
        val targetAccountNumber: String,
        @get: DecimalMin(value = "0.0", inclusive = false)
        val amount: BigDecimal)
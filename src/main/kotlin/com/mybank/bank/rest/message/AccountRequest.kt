package com.mybank.bank.rest.message

import java.math.BigDecimal
import javax.json.bind.annotation.JsonbCreator
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.NotBlank

data class AccountRequest @JsonbCreator constructor(
        @get:NotBlank(message = "Account cannot be blank")
        val accountNumber: String,
        @get: DecimalMin(value = "0.0", inclusive = false)
        val initialBalance: BigDecimal)
package com.mybank.common.values

import java.math.BigDecimal
import java.util.*

data class Money(val value: BigDecimal,
                 val currency: Currency) {

    operator fun compareTo(other: Money): Int {
        return this.value.compareTo(other.value)
    }

    fun add(other: Money): Money {
        return Money(this.value.add(other.value), this.currency)
    }

    fun subtract(other: Money): Money {
        return Money(this.value.subtract(other.value), this.currency)
    }
}

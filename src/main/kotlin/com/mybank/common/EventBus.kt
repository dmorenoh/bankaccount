package com.mybank.common

import java.util.concurrent.CompletionStage

interface EventBus {
    fun send(event: Event): CompletionStage<Void>
}
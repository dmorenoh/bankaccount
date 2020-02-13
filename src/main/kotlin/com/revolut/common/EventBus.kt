package com.revolut.common

import java.util.concurrent.CompletionStage

interface EventBus {
    fun send(event: Event): CompletionStage<Void>
}
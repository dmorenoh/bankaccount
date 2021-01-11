package com.mybank.common

import java.util.concurrent.CompletionStage

interface CommandBus {
    fun send(command: Command): CompletionStage<Void>
}
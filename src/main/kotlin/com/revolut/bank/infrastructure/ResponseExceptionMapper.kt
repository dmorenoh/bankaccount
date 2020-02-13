package com.revolut.bank.infrastructure

import com.revolut.bank.domain.moneytransfer.MoneyTransferException
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class ResponseExceptionMapper : ExceptionMapper<MoneyTransferException> {
    override fun toResponse(exception: MoneyTransferException): Response {
        return Response.status(Response.Status.CONFLICT)
                .header("Conflict-Reason", exception.message)
                .build()
    }
}
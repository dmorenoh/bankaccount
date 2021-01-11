package com.mybank.bank.infrastructure

import com.mybank.bank.domain.moneytransfer.MoneyTransferException
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class ResponseExceptionMapper : ExceptionMapper<MoneyTransferException> {
    override fun toResponse(exception: MoneyTransferException): Response {
        return Response.status(Response.Status.NOT_FOUND)
                .header("Conflict-Reason", exception.message)
                .build()
    }
}
package com.yscorp.reactiver2dbc.interfaces.exception

import org.springframework.graphql.execution.ErrorType

class ApplicationException(val errorType: ErrorType, override val message: String, val extensions: MutableMap<String, Any>) :
    RuntimeException(message)
package com.yscorp.dgsweb.exception

import com.netflix.graphql.types.errors.ErrorDetail
import com.netflix.graphql.types.errors.ErrorType


class ProblemzErrorDetail : ErrorDetail {

    override fun getErrorType(): ErrorType {
        return ErrorType.UNAUTHENTICATED
    }

    override fun toString(): String {
        return "User validation failed. Check that username & password combination match " +
            "(both are case sensitive)."
    }
}

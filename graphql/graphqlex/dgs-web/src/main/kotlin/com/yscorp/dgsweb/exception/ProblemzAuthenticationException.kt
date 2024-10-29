package com.yscorp.dgsweb.exception

class ProblemzAuthenticationException : RuntimeException("Invalid credential")


class ProblemzPermissionException : java.lang.RuntimeException("You are not allowed to access this operation")

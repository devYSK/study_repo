package com.yscorp.withpush.messagesystem.constant

enum class ResultType(val message: String) {
    SUCCESS("Success."),
    FAILED("failed."),
    INVALID_ARGS("Invalid arguments."),
    NOT_FOUND("Not found."),
    ALREADY_JOINED("Already joined."),
    OVER_LIMIT("Over limit."),
    NOT_JOINED("Not joined."),
    NOT_ALLOWED("Unconnected users included.")
}

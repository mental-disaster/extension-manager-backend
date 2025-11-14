package com.flow.blockext.exception

class ExtensionLimitExceededException(
    message: String,
    cause: Throwable? = null,
) : RuntimeException(message, cause)

package com.flow.blockext.exception.extension

class ExtensionLimitExceededException(
    message: String,
    cause: Throwable? = null,
) : RuntimeException(message, cause)

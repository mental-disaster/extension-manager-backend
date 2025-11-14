package com.flow.blockext.exception.extension

class ExtensionQueryException(
    message: String,
    cause: Throwable? = null,
) : RuntimeException(message, cause)

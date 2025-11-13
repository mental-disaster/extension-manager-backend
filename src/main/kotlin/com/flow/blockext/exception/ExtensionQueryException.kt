package com.flow.blockext.exception

class ExtensionQueryException(
    message: String,
    cause: Throwable? = null,
) : RuntimeException(message, cause)

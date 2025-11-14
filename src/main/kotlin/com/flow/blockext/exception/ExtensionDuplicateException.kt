package com.flow.blockext.exception

class ExtensionDuplicateException(
    message: String,
    cause: Throwable? = null,
) : RuntimeException(message, cause)

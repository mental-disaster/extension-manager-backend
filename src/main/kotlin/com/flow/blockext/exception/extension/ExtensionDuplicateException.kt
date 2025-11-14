package com.flow.blockext.exception.extension

class ExtensionDuplicateException(
    message: String,
    cause: Throwable? = null,
) : RuntimeException(message, cause)

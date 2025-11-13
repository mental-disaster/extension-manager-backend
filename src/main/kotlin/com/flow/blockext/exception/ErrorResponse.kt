package com.flow.blockext.exception

import java.time.OffsetDateTime

data class ErrorResponse(
    val code: String,
    val message: String,
    val timestamp: OffsetDateTime = OffsetDateTime.now(),
)

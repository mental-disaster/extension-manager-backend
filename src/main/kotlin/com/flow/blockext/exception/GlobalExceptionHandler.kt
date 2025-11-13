package com.flow.blockext.exception

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(ExtensionQueryException::class)
    fun handleExtensionQueryException(ex: ExtensionQueryException): ResponseEntity<ErrorResponse> {
        log.error("Extension query failed", ex)
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse(code = "EXTENSION_QUERY_FAILED", message = ex.message ?: "확장자 조회 중 오류"))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        log.warn("Invalid request parameter", ex)
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(code = "INVALID_ARGUMENT", message = ex.message ?: "잘못된 요청입니다."))
    }
}

package com.flow.blockext.exception

import com.flow.blockext.exception.extension.ExtensionDuplicateException
import com.flow.blockext.exception.extension.ExtensionLimitExceededException
import com.flow.blockext.exception.extension.ExtensionQueryException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
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

    @ExceptionHandler(ExtensionDuplicateException::class)
    fun handleExtensionDuplicate(ex: ExtensionDuplicateException): ResponseEntity<ErrorResponse> {
        log.warn("Duplicate extension", ex)
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ErrorResponse(code = "EXTENSION_DUPLICATE", message = ex.message ?: "이미 등록된 확장자입니다."))
    }

    @ExceptionHandler(ExtensionLimitExceededException::class)
    fun handleExtensionLimitExceeded(ex: ExtensionLimitExceededException): ResponseEntity<ErrorResponse> {
        log.warn("Extension limit exceeded", ex)
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ErrorResponse(
                code = "EXTENSION_LIMIT_EXCEEDED",
                message = ex.message ?: "사용자 확장자는 최대 200개까지만 등록 가능합니다."
            ))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        log.warn("Validation failed on request parameter", ex)
        val message = ex.bindingResult.fieldErrors.firstOrNull()?.defaultMessage ?: "잘못된 요청입니다."
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(code = "INVALID_REQUEST", message = message))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        log.warn("Invalid request parameter", ex)
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(code = "INVALID_ARGUMENT", message = ex.message ?: "잘못된 요청입니다."))
    }
}

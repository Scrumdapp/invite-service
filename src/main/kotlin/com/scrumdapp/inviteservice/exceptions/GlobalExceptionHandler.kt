package com.scrumdapp.inviteservice.exceptions

import com.scrumdapp.inviteservice.exceptions.ExceptionResponse
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import kotlin.math.log

@RestControllerAdvice
@Component
class GlobalExceptionHandler(
    private val exceptionService: ExceptionService
) {
    val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<ExceptionResponse> {
        val message = ex.bindingResult.fieldErrors
            .firstOrNull()?.defaultMessage ?: "Validation failed"

        return ResponseEntity
            .status(400)
            .body(ExceptionResponse(code = 400, message = message))
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneralException(ex: Exception): ResponseEntity<ExceptionResponse> {
        logger.error(ex.message, ex.stackTrace)
        val body = exceptionService.mapException(ex)
        return ResponseEntity.status(body.code).body(body)
    }
}
package com.scrumdapp.inviteservice.exceptions

import com.scrumdapp.inviteservice.dto.ApiResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
class GlobalExceptionHandler {

    @ExceptionHandler(AppException::class)
    fun handleAppException(ex: AppException): ResponseEntity<ApiResponse> {
        return ResponseEntity
            .status(ex.status)
            .body(
                ApiResponse(
                    code = ex.status.value(),
                    message = ex.message ?: "Unknown error"
                )
            )
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneralException(ex: Exception): ResponseEntity<ApiResponse> {
        ex.printStackTrace()
        return ResponseEntity
            .status(500)
            .body(
                ApiResponse(
                    code = 500,
                    message = "Something went wrong"
                )
            )
    }
}
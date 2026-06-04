package com.scrumdapp.inviteservice.exceptions

import com.scrumdapp.inviteservice.exceptions.ExceptionResponse
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Service
import org.springframework.web.bind.MissingServletRequestParameterException
import tools.jackson.databind.ObjectMapper

@Service
class ExceptionService {
    private val objectMapper: ObjectMapper = ObjectMapper()

    fun handleException(res: HttpServletResponse, ex: Throwable?) {
        val body = mapException(ex)

        res.status = body.code
        res.contentType = MediaType.APPLICATION_JSON_VALUE

        objectMapper.writeValue(res.outputStream, body)
    }

    fun mapException(ex: Throwable?): ExceptionResponse {

        if (ex == null) {
            return bodyFromHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        }

        return when (ex) {
            is AppException -> {
                ExceptionResponse(
                    code = ex.status.value(),
                    message = ex.message
                )
            }
            is MissingServletRequestParameterException -> {
                ExceptionResponse(
                    HttpStatus.NOT_FOUND.value(),
                    "Not found. Did you add the correct parameters?"
                )
            }
            is AuthenticationException -> {
                bodyFromHttpStatus(HttpStatus.UNAUTHORIZED)
            }
            is AccessDeniedException -> {
                bodyFromHttpStatus(HttpStatus.FORBIDDEN, ex.message)
            }
            else -> {
                bodyFromHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
            }
        }
    }

    private fun bodyFromHttpStatus(
        httpStatusCode: HttpStatus,
        message: String? = httpStatusCode.name.lowercase()): ExceptionResponse {
        return ExceptionResponse(
            httpStatusCode.value(),
            message ?: httpStatusCode.name.lowercase()
        )
    }
}
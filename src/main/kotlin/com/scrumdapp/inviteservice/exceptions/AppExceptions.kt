package com.scrumdapp.inviteservice.exceptions

import org.springframework.http.HttpStatus

open class AppException(
    message: String,
    val status: HttpStatus
) : RuntimeException(message)
class NotFoundException(message: String = "Resource not found") :
    AppException(message, HttpStatus.NOT_FOUND)

class ForbiddenException(message: String = "Access denied") :
    AppException(message, HttpStatus.FORBIDDEN)

class BadRequestException(message: String = "Bad request") :
    AppException(message, HttpStatus.BAD_REQUEST)

class UnauthorizedException(message: String = "Unauthorised") :
    AppException(message, HttpStatus.UNAUTHORIZED)

class ServerException(message: String = "Internal server error") :
    AppException(message, HttpStatus.INTERNAL_SERVER_ERROR)
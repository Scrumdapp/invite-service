package com.scrumdapp.inviteservice.exceptions

import org.springframework.http.HttpStatus

class NotFoundException(message: String) : AppException(message, HttpStatus.NOT_FOUND)
class BadRequestException(message: String) : AppException(message, HttpStatus.BAD_REQUEST)

open class AppException(message: String, val status: HttpStatus) : RuntimeException(message)
package com.scrumdapp.inviteservice.exceptions

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class CustomAccessDeniedHandler(): AccessDeniedHandler {

    private val exceptionService = ExceptionService()

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        exceptionService.handleException(response, accessDeniedException)
    }
}

@Component
class CustomAuthEntryPointHandler(): AuthenticationEntryPoint {

    private val exceptionService = ExceptionService()

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        exceptionService.handleException(response, authException)
    }
}
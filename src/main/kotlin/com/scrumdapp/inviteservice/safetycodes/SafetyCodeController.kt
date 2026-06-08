package com.scrumdapp.inviteservice.safetycodes

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

data class SafetyCodeResponse(
    val valid: Boolean,
)

@RestController
@RequestMapping("/invites/safety")
class SafetyCodeController(
    private val safetyCodeService: SafetyCodeService
) {

    @GetMapping
    fun validateToken(
        @RequestParam("token") token: String
    ): SafetyCodeResponse {
        return SafetyCodeResponse(
            valid = safetyCodeService.validateCode(token)
        )
    }
}
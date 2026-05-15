package com.scrumdapp.inviteservice.dto

import jakarta.validation.constraints.Future
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

data class CreateInviteDto(
    @field:NotBlank
    val password: String,

    @field:Future
    val expiresAt: LocalDateTime
)


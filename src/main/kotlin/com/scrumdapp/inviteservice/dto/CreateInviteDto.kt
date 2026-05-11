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

data class AcceptInviteDto(
    val userId: Int,
    val token: String,
    val password: String
)

data class InviteResponseDto(
    val id: Int,
    val groupId: Int,
    val token: String,
    val expiresAt: String,
    val isActive: Boolean
)

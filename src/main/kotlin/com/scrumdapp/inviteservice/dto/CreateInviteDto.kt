package com.scrumdapp.inviteservice.dto

data class CreateInviteDto(
    val password: String
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

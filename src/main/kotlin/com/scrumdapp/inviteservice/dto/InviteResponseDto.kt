package com.scrumdapp.inviteservice.dto

data class InviteResponseDto(
    val id: Int,
    val groupId: Int,
    val token: String,
    val expiresAt: String,
    val isActive: Boolean
)

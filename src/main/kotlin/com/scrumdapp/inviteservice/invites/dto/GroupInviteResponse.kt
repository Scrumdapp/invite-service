package com.scrumdapp.inviteservice.invites.dto

data class GroupInviteResponse(
    val token: String,
    val expiresAt: String,
    val isActive: Boolean
)

package com.scrumdapp.inviteservice.invites.dto

data class GroupInviteResponse(
    val id: Int,
    val groupId: Long,
    val token: String,
    val expiresAt: String?,
    val isActive: Boolean
)

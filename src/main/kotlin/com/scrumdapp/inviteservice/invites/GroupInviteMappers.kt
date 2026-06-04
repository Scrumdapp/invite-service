package com.scrumdapp.inviteservice.invites

import com.scrumdapp.inviteservice.invites.GroupInvite
import com.scrumdapp.inviteservice.invites.dto.CreateInviteDto
import com.scrumdapp.inviteservice.invites.dto.GroupInviteResponse
import java.time.LocalDateTime

fun CreateInviteDto.toEntity(groupId: Int, passwordHash: String?): GroupInvite {
    return GroupInvite().apply {
        this.groupId = groupId
        if (passwordHash != null) {
            this.passwordHash = passwordHash
        }
        this.expiresAt = this@toEntity.expiresAt ?: LocalDateTime.now().plusDays(1)
    }
}

fun GroupInvite.toResponseDto(): GroupInviteResponse {
    return GroupInviteResponse(
        id = id,
        groupId = groupId,
        token = token,
        expiresAt = expiresAt.toString(),
        isActive = isActive
    )
}
package com.scrumdapp.inviteservice.invites

import com.scrumdapp.inviteservice.invites.dto.CreateInviteDto
import com.scrumdapp.inviteservice.invites.dto.GroupInviteResponse
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


fun CreateInviteDto.toEntity(groupId: Int, passwordHash: String?, expiresAt: LocalDateTime): GroupInvite {
    return GroupInvite().apply {
        this.groupId = groupId.toLong()
        if (passwordHash != null) {
            this.passwordHash = passwordHash
        }
        this.expiresAt = expiresAt
    }
}

fun GroupInvite.toResponseDto(): GroupInviteResponse {
    return GroupInviteResponse(
        id = id,
        groupId = groupId,
        token = token,
        expiresAt = "${expiresAt.format(DateTimeFormatter.ISO_DATE_TIME)}Z",
        isActive = isActive
    )
}
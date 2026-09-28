package com.scrumdapp.inviteservice.invites

import com.scrumdapp.inviteservice.invites.dto.CreateInviteDto
import com.scrumdapp.inviteservice.invites.dto.GroupInviteResponse
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


fun CreateInviteDto.toEntity(groupId: Long, expiresAt: LocalDateTime): GroupInvite {
    return GroupInvite().apply {
        this.groupId = groupId
        this.expiresAt = expiresAt
    }
}

fun GroupInvite.toResponseDto(): GroupInviteResponse {
    return GroupInviteResponse(
        token = token.toString(),
        expiresAt = "${expiresAt.format(DateTimeFormatter.ISO_DATE_TIME)}Z",
        isActive = isActive
    )
}
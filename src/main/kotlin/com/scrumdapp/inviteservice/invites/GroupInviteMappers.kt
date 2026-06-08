package com.scrumdapp.inviteservice.invites

import com.scrumdapp.inviteservice.invites.dto.CreateInviteDto
import com.scrumdapp.inviteservice.invites.dto.GroupInviteResponse
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


fun CreateInviteDto.toEntity(groupId: Int, passwordHash: String?): GroupInvite {
    val parsedInstant = Instant.parse(this.expiresAt)
    val parsedTime = LocalDateTime.ofInstant(parsedInstant, ZoneId.systemDefault())
    return GroupInvite().apply {
        this.groupId = groupId.toLong()
        if (passwordHash != null) {
            this.passwordHash = passwordHash
        }
        this.expiresAt = parsedTime
    }
}

fun GroupInvite.toResponseDto(): GroupInviteResponse {
    return GroupInviteResponse(
        id = id,
        groupId = groupId,
        token = token,
        expiresAt = expiresAt.format(DateTimeFormatter.ISO_INSTANT),
        isActive = isActive
    )
}
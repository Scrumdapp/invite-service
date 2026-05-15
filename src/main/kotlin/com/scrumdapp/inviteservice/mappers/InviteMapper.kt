package com.scrumdapp.inviteservice.mappers

import com.scrumdapp.inviteservice.dto.CreateInviteDto
import com.scrumdapp.inviteservice.dto.InviteResponseDto
import com.scrumdapp.inviteservice.entities.GroupInvite

fun CreateInviteDto.toEntity(groupId: Int, passwordHash: String?): GroupInvite {
    return GroupInvite().apply {
        this.groupId = groupId
        if (passwordHash != null) {
            this.passwordHash = passwordHash
        }
        this.expiresAt = this@toEntity.expiresAt
    }
}
fun GroupInvite.toResponseDto(): InviteResponseDto {
    return InviteResponseDto(
        id = id,
        groupId = groupId,
        token = token,
        expiresAt = expiresAt.toString(),
        isActive = isActive
    )
}
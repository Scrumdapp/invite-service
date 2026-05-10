package com.scrumdapp.inviteservice.mappers

import com.scrumdapp.inviteservice.dto.CreateInviteDto
import com.scrumdapp.inviteservice.dto.InviteResponseDto
import com.scrumdapp.inviteservice.entities.GroupInvite
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

private val encoder = BCryptPasswordEncoder()

fun CreateInviteDto.toEntity(groupId: Int): GroupInvite {
    return GroupInvite().apply {
        this.groupId = groupId
        passwordHash = encoder.encode(this@toEntity.password) ?: ""
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
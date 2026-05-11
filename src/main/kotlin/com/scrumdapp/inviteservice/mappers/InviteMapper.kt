package com.scrumdapp.inviteservice.mappers

import com.scrumdapp.inviteservice.dto.CreateInviteDto
import com.scrumdapp.inviteservice.dto.InviteResponseDto
import com.scrumdapp.inviteservice.entities.GroupInvite
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.time.LocalDateTime

private val encoder = BCryptPasswordEncoder()

fun CreateInviteDto.toEntity(groupId: Int): GroupInvite {
    val now = LocalDateTime.now()
    require(expiresAt.isAfter(now)) { "expiresAt must be in the future" }
    require(expiresAt.isBefore(now.plusDays(1))) { "expiresAt may not be more than 1 day in the future" }

    return GroupInvite().apply {
        this.groupId = groupId
        this.passwordHash = encoder.encode(this@toEntity.password) ?: ""
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
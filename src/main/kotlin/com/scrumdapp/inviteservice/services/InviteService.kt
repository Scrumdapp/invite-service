package com.scrumdapp.inviteservice.services

import com.scrumdapp.inviteservice.dto.CreateInviteDto
import com.scrumdapp.inviteservice.dto.InviteResponseDto
import com.scrumdapp.inviteservice.mappers.toEntity
import com.scrumdapp.inviteservice.mappers.toResponseDto
import com.scrumdapp.inviteservice.repositories.GroupInviteRepository
import org.springframework.stereotype.Service

@Service
class InviteService(
    private val groupInviteRepository: GroupInviteRepository
) {
    fun create(groupId: Int, dto: CreateInviteDto): InviteResponseDto {
        val invite = dto.toEntity(groupId)
        return groupInviteRepository.save(invite).toResponseDto()
    }
}
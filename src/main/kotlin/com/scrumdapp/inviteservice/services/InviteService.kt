package com.scrumdapp.inviteservice.services

import com.scrumdapp.inviteservice.dto.AcceptInviteDto
import com.scrumdapp.inviteservice.dto.CreateInviteDto
import com.scrumdapp.inviteservice.dto.InviteResponseDto
import com.scrumdapp.inviteservice.exceptions.BadRequestException
import com.scrumdapp.inviteservice.exceptions.NotFoundException
import com.scrumdapp.inviteservice.exceptions.UnauthorizedException
import com.scrumdapp.inviteservice.mappers.toEntity
import com.scrumdapp.inviteservice.mappers.toResponseDto
import com.scrumdapp.inviteservice.repositories.GroupInviteRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Service
class InviteService(
    private val groupInviteRepository: GroupInviteRepository

) {
    private val encoder = BCryptPasswordEncoder()
    fun create(groupId: Int, dto: CreateInviteDto): InviteResponseDto {
        val invite = dto.toEntity(groupId)
        return groupInviteRepository.save(invite).toResponseDto()
    }

    fun getByGroup(groupId: Int): List<InviteResponseDto> {
        return groupInviteRepository.findAllByGroupId(groupId).map { it.toResponseDto() }
    }

    fun getById(inviteId: Int, token: String): InviteResponseDto {
        val invite = groupInviteRepository.findById(inviteId)
            .orElseThrow { NotFoundException("Invite not found") }

        if (invite.token != token) throw UnauthorizedException("Invalid token")
        if (!invite.isActive) throw BadRequestException("Invite is no longer active")
        if (invite.expiresAt.isBefore(LocalDateTime.now())) throw BadRequestException("Invite has expired, ask for a new one!")

        return invite.toResponseDto()
    }

    fun accept(inviteId: Int, dto: AcceptInviteDto) {
        val invite = groupInviteRepository.findById(inviteId)
            .orElseThrow { NotFoundException("Invite not found") }

        if (invite.token != dto.token) throw UnauthorizedException("Invalid token")
        if (!invite.isActive) throw BadRequestException("Invite is no longer active")
        if (invite.expiresAt.isBefore(LocalDateTime.now())) throw BadRequestException("Invite has expired, ask for a new one!")
        if (!encoder.matches(dto.password, invite.passwordHash)) throw UnauthorizedException("Invalid password")
    }

    fun delete(inviteId: Int) {
        val invite = groupInviteRepository.findById(inviteId)
            .orElseThrow { NotFoundException("Invite not found") }
        groupInviteRepository.delete(invite)
    }
}
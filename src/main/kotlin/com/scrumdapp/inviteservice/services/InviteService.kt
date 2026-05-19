package com.scrumdapp.inviteservice.services

import com.scrumdapp.inviteservice.dto.AcceptInviteDto
import com.scrumdapp.inviteservice.dto.CreateInviteDto
import com.scrumdapp.inviteservice.dto.GroupUserResponseDto
import com.scrumdapp.inviteservice.dto.InviteResponseDto
import com.scrumdapp.inviteservice.exceptions.BadRequestException
import com.scrumdapp.inviteservice.exceptions.ForbiddenException
import com.scrumdapp.inviteservice.exceptions.NotFoundException
import com.scrumdapp.inviteservice.exceptions.UnauthorizedException
import com.scrumdapp.inviteservice.mappers.toEntity
import com.scrumdapp.inviteservice.mappers.toResponseDto
import com.scrumdapp.inviteservice.repositories.GroupInviteRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import org.springframework.web.client.RestClient
import org.springframework.http.MediaType

@Service
class InviteService(
    private val groupInviteRepository: GroupInviteRepository,
    private val encryptionService: EncryptionService
) {
    private val restClient = RestClient.create()
    fun create(groupId: Int, dto: CreateInviteDto, userRole: String): InviteResponseDto {
        if (userRole != "docent") throw ForbiddenException("Only teachers can create invites")

        val now = LocalDateTime.now()
        if (!dto.expiresAt.isAfter(now)) throw BadRequestException("expiresAt must be in the future")
        if (!dto.expiresAt.isBefore(now.plusDays(1))) throw BadRequestException("expiresAt may not be more than 1 day in the future")

        val passwordHash = encryptionService.encode(dto.password)
        val invite = dto.toEntity(groupId, passwordHash)
        return groupInviteRepository.save(invite).toResponseDto()
    }

    fun getByGroup(groupId: Int, userRole: String): List<InviteResponseDto> {
        if (userRole != "docent") throw ForbiddenException("Only teachers can view invites")
        return groupInviteRepository.findAllByGroupId(groupId).map { it.toResponseDto() }
    }

    fun getById(inviteId: Int, token: String): InviteResponseDto {
        val invite = groupInviteRepository.findById(inviteId)
            .orElseThrow { NotFoundException("Invite not found") }

        if (invite.token != token) throw UnauthorizedException("Invalid token")
        if (!invite.isActive) throw BadRequestException("Invite is no longer active")
        if (invite.expiresAt.isBefore(LocalDateTime.now())) throw BadRequestException("Invite has expired")

        return invite.toResponseDto()
    }

    fun accept(inviteId: Int, dto: AcceptInviteDto, authorization: String) {
        val invite = groupInviteRepository.findById(inviteId)
            .orElseThrow { NotFoundException("Invite not found") }

        if (invite.token != dto.token) throw UnauthorizedException("Invalid token")
        if (!invite.isActive) throw BadRequestException("Ïnvite has been deactivated or has expired")
        if (invite.expiresAt.isBefore(LocalDateTime.now())) throw BadRequestException("Ïnvite has been deactivated or has expired")
        if (!encryptionService.matches(dto.password, invite.passwordHash)) throw UnauthorizedException("Invalid password")

        val users = restClient.get()
            .uri("http://localhost:3001/groups/${invite.groupId}/users")
            .header("Authorization", authorization)
            .retrieve()
            .body(Array<GroupUserResponseDto>::class.java)

        val alreadyInGroup = users?.any { it.userId == dto.userId.toLong() }
        if (alreadyInGroup == true) throw BadRequestException("User is already part of this group")

        restClient.post()
            .uri("http://localhost:3001/groups/${invite.groupId}/users")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", authorization)
            .body(mapOf("userId" to dto.userId))
            .retrieve()
            .toBodilessEntity()
    }

    fun delete(inviteId: Int, userRole: String) {
        if (userRole != "docent") throw ForbiddenException("Only teachers can delete invites")
        val invite = groupInviteRepository.findById(inviteId)
            .orElseThrow { NotFoundException("Invite not found") }
        groupInviteRepository.delete(invite)
    }
}
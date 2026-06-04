package com.scrumdapp.inviteservice.invites

import com.scrumdapp.inviteservice.exceptions.BadRequestException
import com.scrumdapp.inviteservice.exceptions.ForbiddenException
import com.scrumdapp.inviteservice.exceptions.NotFoundException
import com.scrumdapp.inviteservice.exceptions.UnauthorizedException
import com.scrumdapp.inviteservice.invites.dto.AcceptInviteDto
import com.scrumdapp.inviteservice.invites.dto.CreateInviteDto
import com.scrumdapp.inviteservice.invites.dto.GroupInviteResponse
import com.scrumdapp.inviteservice.encryption.EncryptionService
import com.scrumdapp.inviteservice.safetycodes.SafetyCodeService
import com.scrumdapp.passportplugin.jwt.PassportContent
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import java.time.LocalDateTime

@Service
class GroupInviteService(
    private val groupInviteRepository: GroupInviteRepository,
    private val encryptionService: EncryptionService,
    private val safetyCodeService: SafetyCodeService
) {
    private val restClient = RestClient.create()

    @Value("\${group.service.url}")
    private lateinit var groupServiceUrl: String

    fun createInvite(groupId: Int, dto: CreateInviteDto): GroupInviteResponse {
        val now = LocalDateTime.now()
        dto.expiresAt?.isBefore(now.plusDays(1))?.let { if (!it) throw BadRequestException("An invite can only last for 24 hours") }

        val passwordHash = encryptionService.encode(dto.password)
        val invite = dto.toEntity(groupId, passwordHash)
        return groupInviteRepository.save(invite).toResponseDto()
    }

    fun getInvitesByGroup(groupId: Int): List<GroupInviteResponse> {
        return groupInviteRepository.findAllByGroupId(groupId).map { it.toResponseDto() }
    }

    fun getInviteById(inviteId: Int, token: String): GroupInviteResponse {
        val invite = groupInviteRepository.findById(inviteId)
            .orElseThrow { NotFoundException("Invite not found") }
        checkInviteValidity(invite, token)
        return invite.toResponseDto()
    }

    fun acceptInvite(inviteId: Int, dto: AcceptInviteDto, authorization: String, userId: Long): Boolean {
        val invite = groupInviteRepository.findById(inviteId)
            .orElseThrow { NotFoundException("Invite not found") }

        val safetyCode = safetyCodeService.createCode(invite)

        if (!encryptionService.matches(dto.password, invite.passwordHash)) throw UnauthorizedException("Invalid password")
        checkInviteValidity(invite, dto.token)

        val response = restClient.post()
            .uri("$groupServiceUrl/groups/${invite.groupId}/users")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", authorization)
            .body(mapOf("userId" to userId))
            .retrieve()
            .onStatus({ it.is4xxClientError }) { _, response ->
                throw BadRequestException("Could not add user to group")
            }
            .toBodilessEntity()
        return true
    }

    fun delete(inviteId: Int, passport: PassportContent) {
        val invite = groupInviteRepository.findById(inviteId)
            .orElseThrow { NotFoundException("Invite not found") }
        groupInviteRepository.delete(invite)
    }

    private fun checkInviteValidity(invite: GroupInvite, token: String) {
        if (invite.token != token) throw UnauthorizedException("Invalid token")
        if (!invite.isActive) throw ForbiddenException("Ïnvite has been deactivated or has expired")
        if (invite.expiresAt.isBefore(LocalDateTime.now())) throw ForbiddenException("Ïnvite has been deactivated or has expired")
    }
}
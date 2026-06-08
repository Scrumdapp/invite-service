package com.scrumdapp.inviteservice.invites

import com.scrumdapp.inviteservice.exceptions.BadRequestException
import com.scrumdapp.inviteservice.exceptions.ForbiddenException
import com.scrumdapp.inviteservice.exceptions.NotFoundException
import com.scrumdapp.inviteservice.exceptions.UnauthorizedException
import com.scrumdapp.inviteservice.invites.dto.AcceptInviteDto
import com.scrumdapp.inviteservice.invites.dto.CreateInviteDto
import com.scrumdapp.inviteservice.invites.dto.GroupInviteResponse
import com.scrumdapp.inviteservice.encryption.EncryptionService
import com.scrumdapp.inviteservice.exceptions.ServerException
import com.scrumdapp.inviteservice.safetycodes.SafetyCodeService
import com.scrumdapp.passportplugin.jwt.PassportContent
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

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
        val parsedInstant = Instant.parse(dto.expiresAt)
        val parsedTime = LocalDateTime.ofInstant(parsedInstant, ZoneId.systemDefault())

        if (parsedTime.isBefore(now) || parsedTime.isAfter(now.plusDays(7)))
            throw BadRequestException(message = "Expires at can only be a maximum of 7 days in the future")

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
        validateInvite(invite, token)
        return invite.toResponseDto()
    }

    fun acceptInvite(inviteId: Int, dto: AcceptInviteDto, authorization: String, userId: Long): Boolean {
        val invite = groupInviteRepository.findById(inviteId)
            .orElseThrow { NotFoundException("Invite not found") }

        val safetyCode = safetyCodeService.createCode(invite)

        if (!encryptionService.matches(dto.password, invite.passwordHash)) throw UnauthorizedException("Invalid password")
        validateInvite(invite, dto.token)

        val response = restClient.post()
            .uri("$groupServiceUrl/groups/${invite.groupId}/users?token=$safetyCode")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", authorization)
            .body(mapOf("user_id" to userId))
            .retrieve()
            .onStatus({ it.is4xxClientError }) { _, response ->
                throw ServerException("Something went wrong adding user to group")
            }
            .toBodilessEntity()
        return true
    }

    fun delete(inviteId: Int, passport: PassportContent) {
        val invite = groupInviteRepository.findById(inviteId)
            .orElseThrow { NotFoundException("Invite not found") }
        groupInviteRepository.delete(invite)
    }

    private fun validateInvite(invite: GroupInvite, token: String) {
        if (invite.token != token) throw UnauthorizedException("Invalid token")
        if (!invite.isActive) throw ForbiddenException("Ïnvite has been deactivated or has expired")
        if (invite.expiresAt.isBefore(LocalDateTime.now())) throw ForbiddenException("Ïnvite has been deactivated or has expired")
    }
}
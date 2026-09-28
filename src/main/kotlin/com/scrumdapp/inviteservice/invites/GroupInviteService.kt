package com.scrumdapp.inviteservice.invites

import com.scrumdapp.inviteservice.exceptions.BadRequestException
import com.scrumdapp.inviteservice.exceptions.NotFoundException
import com.scrumdapp.inviteservice.invites.dto.CreateInviteDto
import com.scrumdapp.inviteservice.invites.dto.GroupInviteResponse
import com.scrumdapp.inviteservice.groups.GroupRequestService
import com.scrumdapp.inviteservice.safetycodes.SafetyCodeService
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

@Service
class GroupInviteService(
    private val groupInviteRepository: GroupInviteRepository,
    private val groupRequestService: GroupRequestService,
    private val safetyCodeService: SafetyCodeService
) {

    fun getById(uuid: UUID): GroupInviteResponse {

        val invite = groupInviteRepository.findFirstByToken(uuid)
            ?: throw NotFoundException(message = "Invite not found")

        checkValidity(invite)

        return invite.toResponseDto()
    }

    fun getByGroup(groupId: Long): List<GroupInviteResponse> {
        return groupInviteRepository.findAllByGroupId(groupId)
            .map { it.toResponseDto() }
    }

    fun create(dto: CreateInviteDto): GroupInviteResponse {

        var expiryTime = LocalDateTime.now(ZoneId.of("UTC")).plusHours(12)
        if (dto.expiresAt != null) {
            val parsedTime = parseToDateTime(dto.expiresAt)
            val curTime = LocalDateTime.now()
            if (parsedTime.isBefore(curTime) || parsedTime.isAfter(curTime.plusDays(7))) {
                throw BadRequestException(message = "Expiry date must be in the future and at most 7 days into the future")
            }

            expiryTime = parsedTime
        }

        return groupInviteRepository.save(dto.toEntity(dto.groupId, expiryTime)).toResponseDto()
    }

    fun delete(uuid: UUID, userGroups: List<Int>) {

        val invite = groupInviteRepository.findFirstByToken(uuid)
            ?: throw NotFoundException(message = "Invite not found")

        if (!userGroups.contains(invite.groupId.toInt())) {
            throw NotFoundException("Invite Not Found")
        }

        groupInviteRepository.delete(invite)
    }

    fun acceptInvite(uuid: UUID, userId: Long, userGroups: List<Int>) {
        val invite = groupInviteRepository.findFirstByToken(uuid)
            ?: throw NotFoundException(message = "Invite not found")

        if (userGroups.contains(invite.groupId.toInt())) {
            throw BadRequestException("User is already part of group")
        }
        checkValidity(invite)

        val safetyCode = safetyCodeService.createCode(invite)
        groupRequestService.joinGroup(safetyCode, invite.groupId, userId)
    }


    private fun checkValidity(invite: GroupInvite) {
        if (invite.expiresAt.isBefore(LocalDateTime.now())) {
            throw NotFoundException(message = "Invite has either expired or could not be found")
        } else if (!invite.isActive) {
            throw NotFoundException(message = "Invite has either expired or could not be found")
        }
    }

    private fun parseToDateTime(time: String): LocalDateTime {
        val instantTime = Instant.parse(time)
        return LocalDateTime.ofInstant(instantTime, ZoneId.of("UTC"))
    }
}
package com.scrumdapp.inviteservice.servicerequests.groups

import com.scrumdapp.inviteservice.exceptions.DownStreamException
import com.scrumdapp.inviteservice.safetycodes.SafetyCodeService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Primary
@Profile("mock-service")
class GroupRequestMockService(
    private val safetyCodeService: SafetyCodeService,
): GroupRequestService {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    init {
        logger.info("Using mock group service")
    }

    override fun acceptInvite(groupId: Long, userId: Long, safetyCode: String): GroupInviteResponse {
        if (!safetyCodeService.validateCode(safetyCode)) {

            // This would simulate the group service's response, slightly different message however
            throw DownStreamException("Invalid code was provided")
        }

        return GroupInviteResponse(
            groupId,
            "dummy group",
            "5",
            true,
            emptyList(),
            2
        )
    }
}
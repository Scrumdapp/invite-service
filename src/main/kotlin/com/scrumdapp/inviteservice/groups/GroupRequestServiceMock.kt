package com.scrumdapp.inviteservice.groups

import com.scrumdapp.inviteservice.exceptions.BadRequestException
import com.scrumdapp.inviteservice.safetycodes.SafetyCodeService
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

@Service
@Primary
@ConditionalOnBooleanProperty(name = ["USE_MOCK_SERVICE"], havingValue = true)
class GroupRequestServiceMock(
    private val safetyCodeService: SafetyCodeService
): GroupRequestService {

    private val logger = LoggerFactory.getLogger(GroupRequestServiceImpl::class.java)

    init {
        logger.warn("Using mock group service!")
    }

    override fun joinGroup(
        safetyCode: String,
        groupId: Long,
        userId: Long
    ): GroupJoinResponse {
        logger.info("Using mock group service to validate a request")

        if (!safetyCodeService.validateCode(safetyCode)) {
            throw BadRequestException("Safety code could not be validated by group mock service")
        }

        return GroupJoinResponse(
            groupId,
            "Mock group"
        )
    }
}
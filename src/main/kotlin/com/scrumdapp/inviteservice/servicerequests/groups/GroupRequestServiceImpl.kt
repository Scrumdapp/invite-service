package com.scrumdapp.inviteservice.servicerequests.groups

import com.scrumdapp.inviteservice.servicerequests.RequestService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service

@Service
class GroupRequestServiceImpl(
    @Value($$"${group.service.url}") private val groupServiceUrl: String,
    private val requestService: RequestService,
): GroupRequestService {

    override fun acceptInvite(groupId: Long, userId: Long, safetyCode: String): GroupInviteResponse {
        val uri = "$groupServiceUrl/groups/$groupId/users?token=$safetyCode"
        return requestService.performServiceRequest(uri, HttpMethod.POST, mapOf("user_id" to userId), GroupInviteResponse::class.java)
    }
}
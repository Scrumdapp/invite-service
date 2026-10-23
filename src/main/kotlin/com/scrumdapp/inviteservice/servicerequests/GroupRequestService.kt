package com.scrumdapp.inviteservice.servicerequests

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service

data class GroupInviteResponse(
    val id: Long,
    val name: String?,
    val background_preference: String?,
    val is_active: Boolean,
    val feature: List<Long>,
    val group_owner: Long
)

@Service
class GroupRequestService(
    @Value($$"${group.service.url}") private val groupServiceUrl: String,
    private val requestService: RequestService,
) {

    fun acceptInvite(groupId: Long, userId: Long, safetyCode: String) {
        val uri = "$groupServiceUrl/groups/$groupId/users?token=$safetyCode"
        requestService.performServiceRequest(uri, HttpMethod.POST, mapOf("user_id" to userId), GroupInviteResponse::class.java)
    }
}
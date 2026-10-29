package com.scrumdapp.inviteservice.servicerequests.groups

interface GroupRequestService {
    fun acceptInvite(groupId: Long, userId: Long, safetyCode: String): GroupInviteResponse
}


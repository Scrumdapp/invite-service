package com.scrumdapp.inviteservice.groups

interface GroupRequestService {

    fun joinGroup(safetyCode: String, groupId: Long, userId: Long): GroupJoinResponse
}
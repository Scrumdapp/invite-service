package com.scrumdapp.inviteservice.servicerequests.groups

data class GroupInviteResponse(
    val id: Long,
    val name: String?,
    val background_preference: String?,
    val is_active: Boolean,
    val feature: List<Long>,
    val group_owner: Long
)
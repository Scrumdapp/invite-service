package com.scrumdapp.inviteservice.dto

data class GroupUserResponseDto(
    val userId: Long,
    val groupId: Long,
    val firstName: String?,
    val lastName: String?
)
package com.scrumdapp.inviteservice.dto

data class AcceptInviteDto(
    val userId: Int,
    val token: String,
    val password: String
)
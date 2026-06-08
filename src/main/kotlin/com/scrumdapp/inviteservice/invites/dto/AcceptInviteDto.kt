package com.scrumdapp.inviteservice.invites.dto

data class AcceptInviteDto(
    val token: String,
    val password: String
)
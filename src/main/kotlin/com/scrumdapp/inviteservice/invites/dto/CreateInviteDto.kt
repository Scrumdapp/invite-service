package com.scrumdapp.inviteservice.invites.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class CreateInviteDto(
    @field:NotBlank(message = "Password must be provided")
    @field:Size(min = 1, max = 20, message = "Password must be between 1 and 20 characters.") // Following the front-end here....
    @field:Pattern(regexp = "[a-zA-Z0-9]+", message = "Password can only contain letters and numbers") // Following the front-end here again....
    val password: String,

//    @field:Pattern(regexp = """^(?:[1-9]\\d{3}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1\\d|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[1-9]\\d(?:0[48]|[2468][048]|[13579][26])|(?:[2468][048]|[13579][26])00)-02-29)T(?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d(?:Z|[+-][01]\\d:[0-5]\\d)\$"""
//        , message = "ExpiresAt must be in a valid iso time format"
//    )
    val expiresAt: String?
)


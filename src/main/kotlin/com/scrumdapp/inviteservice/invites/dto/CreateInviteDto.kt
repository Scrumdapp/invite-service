package com.scrumdapp.inviteservice.invites.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class CreateInviteDto(

    val groupId: Long,

    @field:Pattern(regexp = """(?:19|20)\d{2}-(?:0[1-9]|1[0-2])-(?:0[1-9]|[12]\d|3[01])T(?:[01]\d|2[0-3]):[0-5]\d:[0-5]\d.\d{3}Z""", message = "ExpiresAt must be in a valid iso time format"
    )
    val expiresAt: String?
)
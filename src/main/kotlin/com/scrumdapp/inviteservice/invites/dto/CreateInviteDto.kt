package com.scrumdapp.inviteservice.invites.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class CreateInviteDto(
    @field:NotBlank(message = "Password must be provided")
    @field:Size(min = 1, max = 100, message = "Password must be between 1 and 100 characters.") // Following the front-end here....
    @field:Pattern(regexp = "[a-zA-Z0-9]+", message = "Password can only contain letters and numbers") // Following the front-end here again....
    val password: String,

    @field:Pattern(regexp = """(?:19|20)\d{2}-(?:0[1-9]|1[0-2])-(?:0[1-9]|[12]\d|3[01])T(?:[01]\d|2[0-3]):[0-5]\d:[0-5]\d.\d{3}Z""", message = "ExpiresAt must be in a valid iso time format"
    )
    val expiresAt: String?
)


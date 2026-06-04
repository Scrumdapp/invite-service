package com.scrumdapp.inviteservice.invites.dto

import jakarta.validation.constraints.Future
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class CreateInviteDto(
    @field:NotBlank(message = "Password must be provided")
    @field:Size(min = 1, max = 20, message = "Password must be between 1 and 20 characters.") // Following the front-end here....
    @field:Pattern(regexp = "[a-zA-Z0-9]+", message = "Password can only contain letters and numbers") // Following the front-end here again....
    val password: String,

    @field:Future(message = "An invite can only be created in the future")
    val expiresAt: LocalDateTime?
)


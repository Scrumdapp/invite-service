package com.scrumdapp.inviteservice.invites

import com.scrumdapp.inviteservice.exceptions.ForbiddenException
import com.scrumdapp.inviteservice.exceptions.UnauthorizedException
import com.scrumdapp.inviteservice.invites.dto.CreateInviteDto
import com.scrumdapp.inviteservice.invites.dto.GroupInviteResponse
import com.scrumdapp.passportplugin.annotations.Passport
import com.scrumdapp.passportplugin.jwt.PassportContent
import jakarta.validation.Valid
import org.apache.coyote.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/invites")
class InviteController(
    private val inviteService: GroupInviteService
) {

    @GetMapping
    fun getByGroupId(
        @RequestParam(required = true) groupId: Long,
        @Passport passport: PassportContent
    ): List<GroupInviteResponse> {
        if (passport.userGroups?.contains(groupId.toInt()) == false) {
            throw ForbiddenException(message = "You don't have permission to access this group")
        }

        return inviteService.getByGroup(groupId)
    }

    @GetMapping("/{id}")
    fun getById(
        @PathVariable id: UUID
    ): GroupInviteResponse {
        return inviteService.getById(id)
    }

    @PostMapping
    fun create(
        @Passport passport: PassportContent,
        @Valid @RequestBody dto: CreateInviteDto
    ): GroupInviteResponse {

        if (passport.userGroups.isNullOrEmpty() || !passport.userGroups?.contains(dto.groupId.toInt())!!) {
            throw ForbiddenException(message = "You don't have permission to access this group")
        }

        return inviteService.create(dto)
    }

    @PostMapping("/{id}/accept")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun accept(
        @PathVariable id: UUID,
        @Passport passport: PassportContent,
    ) {
        inviteService.acceptInvite(id, passport.userId.toLong(), passport.userGroups!!)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @PathVariable id: UUID,
        @Passport passport: PassportContent,
    ) {

        if (passport.userGroups.isNullOrEmpty()) {
            throw UnauthorizedException(message = "Could not retrieve user groups")
        }
        inviteService.delete(id, passport.userGroups!!)
    }
}
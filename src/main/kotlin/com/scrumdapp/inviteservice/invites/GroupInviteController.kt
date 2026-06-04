package com.scrumdapp.inviteservice.invites

import com.scrumdapp.inviteservice.exceptions.ForbiddenException
import com.scrumdapp.inviteservice.invites.dto.AcceptInviteDto
import com.scrumdapp.inviteservice.invites.dto.CreateInviteDto
import com.scrumdapp.inviteservice.invites.dto.GroupInviteResponse
import com.scrumdapp.passportplugin.annotations.Passport
import com.scrumdapp.passportplugin.jwt.PassportContent
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/invites")
class InviteController(
    private val inviteService: GroupInviteService
) {
    @GetMapping
    fun getByGroup(
        @RequestParam("group") groupId: Int,
        @Passport passport: PassportContent
    ): List<GroupInviteResponse> {
        if (passport.userGroups == null || passport.userGroups?.contains(groupId) == false) throw ForbiddenException(
            message = "You don't have access to this group"
        )
        return inviteService.getInvitesByGroup(groupId)
    }

    @PostMapping
    fun create(
        @RequestParam("group") groupId: Int,
        @Passport passport: PassportContent,
        @RequestBody @Valid dto: CreateInviteDto,
    ): GroupInviteResponse {
        if (passport.userGroups == null || passport.userGroups?.contains(groupId) == false) throw ForbiddenException(
            message = "You don't have access to this group"
        )
        return inviteService.createInvite(groupId, dto)
    }

    @GetMapping("/{inviteId}")
    fun getById(
        @PathVariable inviteId: Int,
        @RequestParam token: String
    ): ResponseEntity<GroupInviteResponse> {
        return ResponseEntity.ok(inviteService.getInviteById(inviteId, token))
    }

    @PostMapping("/{inviteId}/accept")
    fun accept(
        @PathVariable inviteId: Int,
        @RequestBody @Valid dto: AcceptInviteDto,
        @Passport passport: PassportContent,
        @RequestHeader("Authorization") authorization: String
    ): ResponseEntity<Void> {
        inviteService.acceptInvite(inviteId, dto, authorization, passport.userId.toLong())
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/{inviteId}")
    fun delete(
        @PathVariable inviteId: Int,
        @Passport passport: PassportContent
    ): ResponseEntity<Void> {
        inviteService.delete(inviteId, passport)
        return ResponseEntity.noContent().build()
    }
}
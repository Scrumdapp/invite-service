package com.scrumdapp.inviteservice.controllers

import com.scrumdapp.inviteservice.dto.AcceptInviteDto
import com.scrumdapp.inviteservice.dto.CreateInviteDto
import com.scrumdapp.inviteservice.dto.InviteResponseDto
import com.scrumdapp.inviteservice.services.InviteService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.scrumdapp.passportplugin.annotations.Passport
import com.scrumdapp.passportplugin.jwt.PassportContent
import org.springframework.security.access.prepost.PreAuthorize

@RestController
@RequestMapping("/invites")
class InviteController(
    private val inviteService: InviteService
) {
    @GetMapping
    @PreAuthorize("hasRole('COACH')")
    fun getByGroup(
        @RequestParam("group") groupId: Int,
        @Passport passport: PassportContent
    ): List<InviteResponseDto> {
        return inviteService.getByGroup(groupId, passport)
    }

    @PostMapping
    @PreAuthorize("hasRole('COACH')")
    fun create(
        @RequestParam("group") groupId: Int,
        @RequestBody @Valid dto: CreateInviteDto,
        @Passport userRole: PassportContent
    ): InviteResponseDto {
        return inviteService.create(groupId, dto, userRole)
    }

    @GetMapping("/{inviteId}")
    @PreAuthorize("hasRole('COACH')")
    fun getById(
        @PathVariable inviteId: Int,
        @RequestParam token: String
    ): ResponseEntity<InviteResponseDto> {
        return ResponseEntity.ok(inviteService.getById(inviteId, token))
    }

    @PostMapping("/{inviteId}/accept")
    fun accept(
        @PathVariable inviteId: Int,
        @RequestBody @Valid dto: AcceptInviteDto,
        @RequestHeader("Authorization") authorization: String
    ): ResponseEntity<Void> {
        inviteService.accept(inviteId, dto, authorization)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/{inviteId}")
    @PreAuthorize("hasRole('COACH')")
    fun delete(
        @PathVariable inviteId: Int,
        @Passport passport: PassportContent
    ): ResponseEntity<Void> {
        inviteService.delete(inviteId, passport)
        return ResponseEntity.noContent().build()
    }
}
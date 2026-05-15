package com.scrumdapp.inviteservice.controllers

import com.scrumdapp.inviteservice.dto.AcceptInviteDto
import com.scrumdapp.inviteservice.dto.CreateInviteDto
import com.scrumdapp.inviteservice.dto.InviteResponseDto
import com.scrumdapp.inviteservice.services.InviteService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/invites")
class InviteController(
    private val inviteService: InviteService
) {
    @GetMapping("/")
    fun getByGroup(
        @RequestParam("group") groupId: Int,
        @RequestHeader("Role") userRole: String
    ): List<InviteResponseDto> {
        return inviteService.getByGroup(groupId, userRole)
    }

    @PostMapping("/")
    fun create(
        @RequestParam("group") groupId: Int,
        @RequestBody @Valid dto: CreateInviteDto,
        @RequestHeader("Role") userRole: String
    ): InviteResponseDto {
        return inviteService.create(groupId, dto, userRole)
    }

    @GetMapping("/{inviteId}")
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
    fun delete(
        @PathVariable inviteId: Int,
        @RequestHeader("Role") userRole: String
    ): ResponseEntity<Void> {
        inviteService.delete(inviteId, userRole)
        return ResponseEntity.noContent().build()
    }
}
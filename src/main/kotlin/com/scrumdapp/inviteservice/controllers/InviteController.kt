package com.scrumdapp.inviteservice.controllers

import com.scrumdapp.inviteservice.dto.CreateInviteDto
import com.scrumdapp.inviteservice.dto.InviteResponseDto
import com.scrumdapp.inviteservice.services.InviteService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/invites")
class InviteController(
    private val inviteService: InviteService
) {
    @PostMapping
    fun create(
        @RequestParam("group") groupId: Int,
        @RequestBody dto: CreateInviteDto
    ): ResponseEntity<InviteResponseDto> {
        return ResponseEntity.ok(inviteService.create(groupId, dto))
    }
}
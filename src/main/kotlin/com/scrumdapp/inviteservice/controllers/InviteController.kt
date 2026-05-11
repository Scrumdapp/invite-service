package com.scrumdapp.inviteservice.controllers

import com.scrumdapp.inviteservice.dto.AcceptInviteDto
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
    @GetMapping("/")
    fun getByGroup(@RequestParam("group") groupId: Int): ResponseEntity<List<InviteResponseDto>> {
        return ResponseEntity.ok(inviteService.getByGroup(groupId))
    }

    @PostMapping("/")
    fun create(
        @RequestParam("group") groupId: Int,
        @RequestBody dto: CreateInviteDto
    ): ResponseEntity<InviteResponseDto> {
        return ResponseEntity.ok(inviteService.create(groupId, dto))
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
        @RequestBody dto: AcceptInviteDto
    ): ResponseEntity<Void> {
        inviteService.accept(inviteId, dto)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{inviteId}")
    fun delete(@PathVariable inviteId: Int): ResponseEntity<Void> {
        inviteService.delete(inviteId)
        return ResponseEntity.noContent().build()
    }
}
package com.scrumdapp.inviteservice.invites

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "group_invites")
class GroupInvite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0

    @Column(name = "group_id", nullable = false)
    var groupId: Long = 0

    @Column(nullable = false, unique = true)
    var token: String = UUID.randomUUID().toString()

    @Column(name = "password_hash", nullable = false)
    var passwordHash: String = ""

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "expires_at", nullable = false)
    var expiresAt: LocalDateTime = LocalDateTime.now().plusDays(1)

    @Column(name = "is_active", nullable = false)
    var isActive: Boolean = true
}
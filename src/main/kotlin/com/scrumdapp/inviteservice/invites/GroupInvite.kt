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

    @Column(nullable = false)
    var groupId: Int = 0

    @Column(nullable = false, unique = true)
    var token: String = UUID.randomUUID().toString()

    @Column(nullable = false)
    var passwordHash: String = ""

    @Column(nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()

    @Column(nullable = false)
    var expiresAt: LocalDateTime = LocalDateTime.now().plusDays(1)

    @Column(nullable = false)
    var isActive: Boolean = true
}
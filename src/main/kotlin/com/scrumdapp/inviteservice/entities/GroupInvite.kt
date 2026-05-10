package com.scrumdapp.inviteservice.entities

import jakarta.persistence.*
import java.time.LocalDate
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
    var createdAt: LocalDate = LocalDate.MIN

    @Column(nullable = false)
    var expiresAt: Int = 0

    @Column(nullable = false)
    var isActive: Boolean = true
}
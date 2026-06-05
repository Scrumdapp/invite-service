package com.scrumdapp.inviteservice.safetycodes

import com.scrumdapp.inviteservice.invites.GroupInvite
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Table(name = "invite_safety_codes")
@Entity
class SafetyCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0

    @Column(nullable = false)
    var code: String = ""

    @ManyToOne
    @JoinColumn(name = "group_invite_id")
    var invite: GroupInvite = GroupInvite()
}
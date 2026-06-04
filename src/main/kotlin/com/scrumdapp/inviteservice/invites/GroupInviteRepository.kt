package com.scrumdapp.inviteservice.invites

import com.scrumdapp.inviteservice.invites.GroupInvite
import org.springframework.data.jpa.repository.JpaRepository

interface GroupInviteRepository : JpaRepository<GroupInvite, Int> {
    fun findAllByGroupId(groupId: Int): List<GroupInvite>
}
package com.scrumdapp.inviteservice.repositories

import com.scrumdapp.inviteservice.entities.GroupInvite
import org.springframework.data.jpa.repository.JpaRepository

interface GroupInviteRepository : JpaRepository<GroupInvite, Int> {
    fun findAllByGroupId(groupId: Int): List<GroupInvite>
}
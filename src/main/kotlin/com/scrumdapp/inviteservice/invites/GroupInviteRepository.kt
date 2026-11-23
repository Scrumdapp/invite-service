package com.scrumdapp.inviteservice.invites

import org.springframework.data.jpa.repository.JpaRepository

interface GroupInviteRepository : JpaRepository<GroupInvite, Int> {
    fun findAllByGroupId(groupId: Long): List<GroupInvite>
}
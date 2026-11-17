package com.scrumdapp.inviteservice.groupinvites

import org.springframework.data.jpa.repository.JpaRepository

interface GroupInviteRepository : JpaRepository<GroupInvite, Int> {
    fun findAllByGroupId(groupId: Int): List<GroupInvite>
}
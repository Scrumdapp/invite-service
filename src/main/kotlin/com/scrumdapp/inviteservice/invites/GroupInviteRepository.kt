package com.scrumdapp.inviteservice.invites

import com.scrumdapp.inviteservice.invites.GroupInvite
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface GroupInviteRepository : JpaRepository<GroupInvite, Int> {

    fun findFirstByToken(token: UUID): GroupInvite?
    fun findAllByGroupId(groupId: Long): List<GroupInvite>
}
package com.scrumdapp.inviteservice.safetycodes

import com.scrumdapp.inviteservice.safetycodes.SafetyCode
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SafetyCodeRepository: JpaRepository<SafetyCode, Int> {
    fun findByCode(code: String): SafetyCode?
}
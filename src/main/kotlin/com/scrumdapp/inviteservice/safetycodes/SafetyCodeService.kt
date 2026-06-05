package com.scrumdapp.inviteservice.safetycodes

import com.scrumdapp.inviteservice.invites.GroupInvite
import com.scrumdapp.inviteservice.safetycodes.SafetyCode
import com.scrumdapp.inviteservice.safetycodes.SafetyCodeRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SafetyCodeService(
    private val repository: SafetyCodeRepository,
    @Value("\${safetycode.length}") private val codeLength: Int,
) {

    fun createCode(invite: GroupInvite): String {
        val code = generateUrlSafeString(codeLength)
        val safetyCode = SafetyCode().apply {
            this.invite = invite
            this.code = code
        }
        repository.save(safetyCode)
        return code
    }

    fun validateCode(code: String): Boolean {
        val safetyCode = repository.findByCode(code) ?: return false
        repository.delete(safetyCode)
        return true
    }

    private fun generateUrlSafeString(length: Int): String {
        val allowedChars = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..length).map { allowedChars.random() }.joinToString("")
    }
}
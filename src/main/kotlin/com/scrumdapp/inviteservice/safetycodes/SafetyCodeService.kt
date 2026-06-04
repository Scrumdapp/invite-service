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
    @Value("\${safetycode.lifetime}") private val lifetime: Long,
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
        if (!safetyCode.isValid) return false
        if (code != safetyCode.code) return false
        if (LocalDateTime.now().isAfter(safetyCode.createdAt.plusMinutes(lifetime))) return false

        safetyCode.apply {
            isValid = false
        }
        repository.save(safetyCode)
        return true
    }

    private fun generateUrlSafeString(length: Int): String {
        val allowedChars = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..length).map { allowedChars.random() }.joinToString("")
    }
}
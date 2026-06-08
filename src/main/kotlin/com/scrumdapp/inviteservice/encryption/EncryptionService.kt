package com.scrumdapp.inviteservice.encryption

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class EncryptionService {
    private val encoder = BCryptPasswordEncoder(12)

    fun encode(password: String): String? = encoder.encode(password)
    fun matches(password: String, hash: String): Boolean = encoder.matches(password, hash)
}
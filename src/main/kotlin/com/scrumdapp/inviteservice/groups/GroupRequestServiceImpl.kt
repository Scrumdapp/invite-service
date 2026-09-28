package com.scrumdapp.inviteservice.groups

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.scrumdapp.inviteservice.exceptions.ServerException
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.toEntity
import tools.jackson.core.type.TypeReference
import tools.jackson.databind.ObjectMapper

@JsonIgnoreProperties(ignoreUnknown = true)
data class GroupJoinResponse(
    val id: Long,
    val name: String
)

@Service
class GroupRequestServiceImpl: GroupRequestService {

    private val restClient = RestClient.create()
    private val mapper = ObjectMapper()

    @Value("\${group.service.url}")
    private lateinit var groupServiceUrl: String

    override fun joinGroup(safetyCode: String, groupId: Long, userId: Long): GroupJoinResponse {
        val url = "groups/$groupId/users?token=$safetyCode"

        val res = performRequest<GroupJoinResponse>(HttpMethod.POST, url)
        return res
    }

    private fun <T> performRequest(method: HttpMethod, uri: String): T {

        val jwt = SecurityContextHolder.getContext().authentication?.principal as Jwt

        var res: ResponseEntity<String>

        try {
            res = restClient.method(method)
                .uri("$groupServiceUrl/$uri")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer ${jwt.tokenValue}")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity<String>()
        } catch (e: Exception) {
            throw ServerException("Downstream service is unreachable")
        }

        val body = res.body ?: throw ServerException(message = "Unexpected response from downstream service")
        return mapper.readValue(body, object: TypeReference<T>() {})
    }

}
package com.scrumdapp.inviteservice.servicerequests

import com.fasterxml.jackson.databind.ObjectMapper
import com.scrumdapp.inviteservice.exceptions.DownStreamException
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.toEntity

@Service
class RequestService(
    @Value($$"${spring.application.name}") private val appName: String,
) {

    private val restClient = RestClient.create()

    private val mapper = ObjectMapper()

    private fun getAuthHeader(): String {
        val jwt = SecurityContextHolder.getContext().authentication?.principal as Jwt
        return "bearer ${jwt.tokenValue}"
    }

    fun <T> performServiceRequest(uri: String, method: HttpMethod, body: Any?, responseType: Class<T>): T {

        val response = restClient.method(method)
            .uri(uri)
            .header(HttpHeaders.AUTHORIZATION, getAuthHeader())
            .header(HttpHeaders.VIA, appName)
            .accept(MediaType.APPLICATION_JSON)
            .body(body ?: "")
            .retrieve()
            .toEntity<String>()

        if (response.statusCode.is4xxClientError || response.statusCode.is5xxServerError) {
            throw DownStreamException()
        }

        return mapper.readValue(response.body, responseType)
    }
}
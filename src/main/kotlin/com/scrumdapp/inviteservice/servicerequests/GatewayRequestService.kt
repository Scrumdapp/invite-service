package com.scrumdapp.inviteservice.servicerequests

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service

@Service
class GatewayRequestService(
    private val requestService: RequestService,
    @Value($$"${gateway.service.url") private val gatewayServiceUrl: String
) {
    
    fun invalidatePassport() {
        val uri = "$gatewayServiceUrl/api/passports/@me"
        requestService.performServiceRequest(uri, HttpMethod.DELETE, null, Unit::class.java)
    }
}
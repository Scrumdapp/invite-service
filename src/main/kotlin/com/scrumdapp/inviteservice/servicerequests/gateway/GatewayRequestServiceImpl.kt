package com.scrumdapp.inviteservice.servicerequests.gateway

import com.scrumdapp.inviteservice.servicerequests.RequestService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service

@Service
class GatewayRequestServiceImpl(
    private val requestService: RequestService,
    @Value($$"${gateway.service.url") private val gatewayServiceUrl: String
): GatewayRequestService {

    override fun invalidatePassport() {
        val uri = "$gatewayServiceUrl/api/passports/@me"
        requestService.performServiceRequest(uri, HttpMethod.DELETE, null, Unit::class.java)
    }
}
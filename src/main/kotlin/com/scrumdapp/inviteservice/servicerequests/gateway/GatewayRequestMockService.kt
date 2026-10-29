package com.scrumdapp.inviteservice.servicerequests.gateway

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Primary
@Profile("mock-service")
class GatewayRequestMockService(
): GatewayRequestService {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    init {
        logger.info("Using mock gateway")
    }

    override fun invalidatePassport() {
        logger.info("'Invalidated' passport through mock service")
    }
}
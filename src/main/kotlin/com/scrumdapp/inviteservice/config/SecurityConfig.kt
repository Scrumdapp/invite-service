package com.scrumdapp.inviteservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(HttpMethod.GET, "/invites/{inviteId}").permitAll()
                    .requestMatchers(HttpMethod.POST, "/invites/{inviteId}/accept").permitAll()
                    .requestMatchers(HttpMethod.GET, "/invites/").permitAll()
                    .requestMatchers(HttpMethod.POST, "/invites/").permitAll()
                    .requestMatchers(HttpMethod.DELETE, "/invites/{inviteId}").permitAll()
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

            }
        return http.build()
    }
}
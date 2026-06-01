package com.scrumdapp.inviteservice.config

import com.scrumdapp.passportplugin.filters.PassportAuthFilter
import com.scrumdapp.passportplugin.filters.usePassport
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val passportAuthFilter: PassportAuthFilter
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .usePassport(passportAuthFilter)
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(HttpMethod.GET, "/invites/{inviteId}").hasAnyAuthority("COACH")
                    .requestMatchers(HttpMethod.POST, "/invites/{inviteId}/accept").hasAnyAuthority("STUDENT","COACH")
                    .requestMatchers(HttpMethod.GET, "/invites/").hasAnyAuthority("COACH")
                    .requestMatchers(HttpMethod.POST, "/invites/").hasAnyAuthority("COACH")
                    .requestMatchers(HttpMethod.DELETE, "/invites/{inviteId}").hasAnyAuthority("COACH")
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
            }
        return http.build()
    }
}
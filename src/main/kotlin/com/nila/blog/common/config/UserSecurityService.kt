package com.nila.blog.common.config

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service


@Service
class UserSecurityService {
    fun setCurrentUser(authenticationToken: UsernamePasswordAuthenticationToken?) {
        SecurityContextHolder.getContext().authentication = authenticationToken
    }

    val currentUser: String
        get() {
            val authentication = SecurityContextHolder.getContext().authentication
            return authentication.principal as String
        }
}

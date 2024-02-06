package com.nila.blog.common.config

import com.nila.blog.common.aop.ErrorCode
import com.nila.blog.common.aop.exeptions.BlogException
import com.nila.blog.database.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class ApplicationConfig {

    @Autowired
    lateinit var userRepository: UserRepository

    private val log = LoggerFactory.getLogger(JWTVerificationService::class.java)

    @Bean
    fun userDetailsService(): UserDetailsService {
        return UserDetailsService { username: String? ->
            username?.let {
                userRepository.findByUsername(it)
                    .orElseThrow {
                        log.error(ErrorCode.USER_NOT_FOUND.message)
                        BlogException(ErrorCode.USER_NOT_FOUND)
                    }
            }
        }
    }

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userDetailsService())
        authProvider.setPasswordEncoder(passwordEncoder())
        return authProvider
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
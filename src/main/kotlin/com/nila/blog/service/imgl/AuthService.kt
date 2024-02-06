package com.nila.blog.service.imgl

import com.nila.blog.common.aop.ErrorCode
import com.nila.blog.common.aop.exeptions.BlogException
import com.nila.blog.common.config.JWTVerificationService
import com.nila.blog.common.config.JwtService
import com.nila.blog.common.dto.authentication.res.AuthenticationResponse
import com.nila.blog.database.model.User
import com.nila.blog.database.model.enums.Roles
import com.nila.blog.service.IAuthService
import com.nila.blog.service.IUserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService : IAuthService {

    @Autowired
    lateinit var userService: IUserService

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var jwtService: JwtService

    private val log = LoggerFactory.getLogger(JWTVerificationService::class.java)

    override fun singUpUser(user: User, confirmPassword: String): AuthenticationResponse {
        if (user.password != confirmPassword) {
            log.warn(ErrorCode.PASSWORD_CONFIRMATION_MISMATCH.message)
            throw BlogException(ErrorCode.PASSWORD_CONFIRMATION_MISMATCH)
        }

        if (userService.existByUsername(user.username)) {
            log.warn(ErrorCode.DUPLICATE_USERNAME.message)
            throw BlogException(ErrorCode.DUPLICATE_USERNAME)
        }

        val encodedPassword = passwordEncoder.encode(user.password)
        user.password = encodedPassword
        userService.save(user)

        return getJWTResponse(user)
    }

    override fun singIn(): AuthenticationResponse {
        TODO("Not yet implemented")
    }

    private fun getJWTResponse(user: User): AuthenticationResponse {
        val roles = listOf(Roles.USER.name)
        val uuid = listOf(user.id.toString())
        val payload = mapOf(
            JWTVerificationService.CLAIM_ROLES to roles,
            JWTVerificationService.UUID to uuid
        )
        val token = jwtService.getToken(payload, user)
        token.userId = user.id.toString()
        return token
    }
}
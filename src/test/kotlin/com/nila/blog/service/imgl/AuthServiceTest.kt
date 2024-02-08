package com.nila.blog.service.imgl

import com.nila.blog.common.aop.exeptions.BlogException
import com.nila.blog.common.config.JWTVerificationService
import com.nila.blog.common.config.JwtService
import com.nila.blog.common.dto.authentication.res.AuthenticationResponse
import com.nila.blog.database.model.User
import com.nila.blog.database.model.enums.Roles
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

@SpringBootTest
internal class AuthServiceTest {

    @Mock
    private lateinit var userService: UserService

    @Mock
    private lateinit var authenticationManager: AuthenticationManager

    @Mock
    private lateinit var passwordEncoder: PasswordEncoder

    @Mock
    private lateinit var jwtService: JwtService

    @InjectMocks
    private lateinit var authService: AuthService


    @Test
    fun singUpUser() {
        val user = User().apply {
            id = UUID.randomUUID()
            username = "user"
            email = "user@gmail.com"
            password = "user1234"
        }
        val confirmPassword = "user1234"

        val jwt = AuthenticationResponse().apply {
            accessToken = "token"
            tokenType = "Bearer"
            userId = "userid"
        }

        `when`(userService.existByUsernameOrEmail(user.username, user.email)).thenReturn(false)

        `when`(passwordEncoder.encode(user.password)).thenReturn(user.password)

        `when`(jwtService.getToken(
            mapOf(
                JWTVerificationService.CLAIM_ROLES to listOf(Roles.ROLE_USER.name),
                JWTVerificationService.UUID to listOf(user.id.toString())
            ),
            user
        )).thenReturn(jwt)

        val response = authService.singUpUser(user, confirmPassword)

        assertNotNull(response)
    }

    @Test
    fun singUpUserWithPasswordConfirmationMismatch() {
        val user = User().apply {
            username = "user"
            email = "user@gmail.com"
            password = "user1234"
        }
        val confirmPassword = "wrong_password"

        assertThrows(BlogException::class.java) {
            authService.singUpUser(user, confirmPassword)
        }
    }

    @Test
    fun singUpUserWithDuplicateUsername() {
        val user = User().apply {
            username = "user"
            email = "user@gmail.com"
            password = "user1234"
        }
        val confirmPassword = "password"

        `when`(userService.existByUsernameOrEmail(user.username, user.email)).thenReturn(true)

        assertThrows(BlogException::class.java) {
            authService.singUpUser(user, confirmPassword)
        }
    }

    @Test
    fun signIn() {
        val username = "admin"
        val password = "admin123"
        val user = User().apply {
            id = UUID.randomUUID()
            this.username = username
            email = "admin@gmail.com"
            this.password = password
        }

        val jwt = AuthenticationResponse().apply {
            accessToken = "token"
            tokenType = "Bearer"
            userId = user.id.toString()
        }

        `when`(userService.findByUsername(username)).thenReturn(user)

        `when`(jwtService.getToken(
            mapOf(
                JWTVerificationService.CLAIM_ROLES to listOf(Roles.ROLE_USER.name),
                JWTVerificationService.UUID to listOf(user.id.toString())
            ),
            user
        )).thenReturn(jwt)

        val response = authService.singIn(username, password)

        assertNotNull(response)
    }

    @Test
    fun signInWithIncorrectPassword() {
        val username = "userPlus"
        val password = "badPassword"

        `when`(authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password)))
            .thenThrow(BadCredentialsException("Invalid credentials"))

        assertThrows(BlogException::class.java) {
            authService.singIn(username, password)
        }
    }
}
package com.nila.blog.service.imgl

import com.nila.blog.common.aop.ErrorCode
import com.nila.blog.common.aop.exeptions.BlogException
import com.nila.blog.common.dto.user.req.UserEditProfileReq
import com.nila.blog.database.model.User
import com.nila.blog.database.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
internal class UserServiceTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    lateinit var userService: UserService

    @Test
    fun saveUserTest() {
        val user = User().apply {
            id = UUID.randomUUID()
            username = "user"
            email = "user@gmail.com"
            password = "password123"
        }

        `when`(userRepository.save(user)).thenReturn(user)

        userService.save(user)

        `when`(userRepository.findById(user.id!!)).thenReturn(Optional.of(user))

        val retrievedUser = userService.findById(user.id!!)

        assertNotNull(retrievedUser)
        assertEquals(user.id, retrievedUser.id)
        assertEquals(user.username, retrievedUser.username)
        assertEquals(user.email, retrievedUser.email)
    }


    @Test
    fun editProfile() {
        val userId = UUID.randomUUID().toString()
        val user = User().apply {
            id = UUID.fromString(userId)
            username = "user"
            email = "user@gmail.com"
            password = "password123"
        }

        val editProfileReq = UserEditProfileReq().apply {
            username = "userNew"
            email = "userNew@gmail.com"
        }

        `when`(userRepository.isValidUsernameAndEmailForUpdate(user.id!!, editProfileReq.username, editProfileReq.email)).thenReturn(true)

        `when`(userRepository.findById(user.id!!)).thenReturn(Optional.of(user))

        userService.editProfile(userId, editProfileReq)

        assertEquals(editProfileReq.username, user.username)
        assertEquals(editProfileReq.email, user.email)
    }

    @Test
    fun editProfileWithDuplicateUsername() {
        val userId = UUID.randomUUID().toString()
        val user = User().apply {
            id = UUID.fromString(userId)
            username = "user"
            email = "user@gmail.com"
            password = "password123"
        }

        val editProfileReq = UserEditProfileReq().apply {
            username = "duplicateUsername"
            email = "newemail@gmail.com"
        }

        `when`(userRepository.isValidUsernameAndEmailForUpdate(user.id!!, editProfileReq.username, editProfileReq.email)).thenReturn(false)

        `when`(userRepository.findById(user.id!!)).thenReturn(Optional.of(user))

        val exception = assertThrows(BlogException::class.java) {
            userService.editProfile(userId, editProfileReq)
        }

        assertEquals(ErrorCode.DUPLICATE_USERNAME, exception.errorCode)
    }

}
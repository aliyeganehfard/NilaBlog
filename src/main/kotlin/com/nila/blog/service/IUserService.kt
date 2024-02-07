package com.nila.blog.service

import com.nila.blog.database.model.User
import org.springframework.web.multipart.MultipartFile
import java.util.*

interface IUserService {

    fun save(user: User)

    fun uploadUserProfile(image: MultipartFile, userId: String)

    fun existByUsernameOrEmail(username: String, email: String): Boolean

    fun findByUsername(username: String): User

    fun findById(userId: UUID): User
}
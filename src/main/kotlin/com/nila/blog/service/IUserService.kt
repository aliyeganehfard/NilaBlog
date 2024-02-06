package com.nila.blog.service

import com.nila.blog.database.model.User

interface IUserService {

    fun save(user: User)

    fun existByUsername(username: String): Boolean

    fun findByUsername(username: String): User
}
package com.nila.blog.database.repository

import com.nila.blog.database.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, UUID> {

    fun findByUsername(username: String): Optional<User>

    fun existsByUsername(username: String): Boolean
}
package com.nila.blog.database.repository

import com.nila.blog.database.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, UUID> {

    fun findByUsername(username: String): Optional<User>

    fun existsByUsernameOrEmail(username: String, email: String): Boolean

    @Query("""
        SELECT CASE WHEN COUNT(u) > 0 THEN false ELSE TRUE END FROM User u
        WHERE u.id <> :userId AND (u.username = :username OR u.email = :email)
    """)
    fun isValidUsernameAndEmailForUpdate(userId: UUID, username: String, email: String): Boolean
}
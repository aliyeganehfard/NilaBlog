package com.nila.blog.database.repository

import com.nila.blog.database.model.BlogPost
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface BlogPostRepository : JpaRepository<BlogPost, Long> {

    @Query(
        """
        SELECT p FROM BlogPost p
        WHERE p.user.id = :userId
    """
    )
    fun findUserPosts(userId: UUID, pageable: Pageable): List<BlogPost>

    @Query("""
        SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM BlogPost p
        WHERE p.user.id = :userId AND p.id = :postId
    """)
    fun isUserValidToAccess(postId :Long, userId: UUID): Boolean
}
package com.nila.blog.database.repository

import com.nila.blog.database.model.BlogPost
import com.nila.blog.database.model.enums.PostCategory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.sql.Timestamp
import java.util.*

@Repository
interface BlogPostRepository : JpaRepository<BlogPost, Long> {

    @Query(
        """
        SELECT p FROM BlogPost p
        WHERE p.author.id = :userId
    """
    )
    fun findUserPosts(userId: UUID, pageable: Pageable): List<BlogPost>

    @Query(
        """
        SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM BlogPost p
        WHERE p.author.id = :userId AND p.id = :postId
    """
    )
    fun isUserValidToAccess(postId: Long, userId: UUID): Boolean

    @Query(
        """
        SELECT p FROM BlogPost p
        WHERE p.createdAt BETWEEN :startDate AND :finishDate
        AND (COALESCE(:authorId, p.author.id) = p.author.id)
        AND (:category IS NULL OR p.category = :category)
        AND (:keywords IS NULL OR p.keywords IN :keywords) 
    """
    )
    fun findAllPosts(
        startDate: Date?,
        finishDate: Date?,
        authorId: UUID?,
        category: PostCategory?,
        keywords: List<String?>?,
        pageable: Pageable
    ): Optional<List<BlogPost>>
}
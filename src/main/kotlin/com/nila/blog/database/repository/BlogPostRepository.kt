package com.nila.blog.database.repository

import com.nila.blog.database.model.BlogPost
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BlogPostRepository: JpaRepository<BlogPost, Long> {
}
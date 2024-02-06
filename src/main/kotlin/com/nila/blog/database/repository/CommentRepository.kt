package com.nila.blog.database.repository

import com.nila.blog.database.model.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository:JpaRepository<Comment,Long> {
}
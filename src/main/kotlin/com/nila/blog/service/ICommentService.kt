package com.nila.blog.service

import com.nila.blog.common.dto.comment.req.CommentEditReq
import com.nila.blog.database.model.Comment

interface ICommentService {

    fun add(userId: String, comment: Comment)

    fun edit(req: CommentEditReq)

    fun delete(commentId: Long)

    fun findById(postId: Long): Comment
}
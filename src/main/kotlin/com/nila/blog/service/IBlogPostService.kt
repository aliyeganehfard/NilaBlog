package com.nila.blog.service

import com.nila.blog.common.dto.post.req.PostEditReq
import com.nila.blog.common.dto.post.req.PostFindAllReq
import com.nila.blog.database.model.BlogPost

interface IBlogPostService {

    fun add(userId: String, post: BlogPost)

    fun edit(req: PostEditReq)

    fun delete(postId: Long)

    fun findUserPosts(userId: String, page: Int, size: Int): List<BlogPost>

    fun findById(postId: Long): BlogPost

    fun findAllPost(req: PostFindAllReq): List<BlogPost>
}
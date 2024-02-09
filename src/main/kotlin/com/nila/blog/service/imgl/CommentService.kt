package com.nila.blog.service.imgl

import com.nila.blog.common.aop.ErrorCode
import com.nila.blog.common.aop.exeptions.BlogException
import com.nila.blog.common.dto.comment.req.CommentEditReq
import com.nila.blog.database.model.Comment
import com.nila.blog.database.repository.CommentRepository
import com.nila.blog.service.IBlogPostService
import com.nila.blog.service.ICommentService
import com.nila.blog.service.IUserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class CommentService : ICommentService {

    @Autowired
    lateinit var commentRepository: CommentRepository

    @Autowired
    lateinit var postService: IBlogPostService

    @Autowired
    lateinit var userService: IUserService

    private val log = LoggerFactory.getLogger(CommentService::class.java)

    @Transactional
    override fun add(userId: String, comment: Comment) {
        val user = userService.findById(UUID.fromString(userId))
        val post = postService.findById(comment.post!!.id!!)

        comment.user = user
        comment.post = post

        commentRepository.save(comment)
        log.info("user {} save comment with commentId {} for post with postId {}", user.username, comment.id, post.id)
    }

    @Transactional
    @CacheEvict(cacheNames = ["comment"], key = "#req.id")
    override fun edit(req: CommentEditReq) {
        val comment = findById(req.id!!)
        comment.text = req.text
        commentRepository.save(comment)
        log.info("comment with commentId {} updated", comment.id)
    }

    @Transactional
    @CacheEvict(cacheNames = ["comment"], key = "#commentId")
    override fun delete(commentId: Long) {
        val comment = findById(commentId)
        commentRepository.delete(comment)
        log.info("comment with id {} deleted", comment.id)
    }

    @Cacheable(cacheNames = ["comment"], key = "#commentId")
    override fun findById(commentId: Long): Comment {
        return commentRepository.findById(commentId)
            .orElseThrow {
                log.warn(ErrorCode.COMMENT_NOT_FOUND.message)
                BlogException(ErrorCode.COMMENT_NOT_FOUND)
            }
    }
}
package com.nila.blog.service.imgl

import com.nila.blog.common.aop.ErrorCode
import com.nila.blog.common.aop.exeptions.BlogException
import com.nila.blog.common.config.JWTVerificationService
import com.nila.blog.common.dto.post.req.PostEditReq
import com.nila.blog.common.dto.post.req.PostFindAllReq
import com.nila.blog.database.model.BlogPost
import com.nila.blog.database.repository.BlogPostRepository
import com.nila.blog.service.IBlogPostService
import com.nila.blog.service.IUserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Service
class BlogPostService : IBlogPostService {

    @Autowired
    lateinit var postRepository: BlogPostRepository

    @Autowired
    lateinit var userService: IUserService

    @Autowired
    lateinit var jwtService: JWTVerificationService

    private val log = LoggerFactory.getLogger(BlogPostService::class.java)

    @Transactional
    override fun add(userId: String, post: BlogPost) {
        val user = userService.findById(UUID.fromString(userId))
        post.author = user
        postRepository.save(post)
        log.info("add post for user {} with post id {}", user.username, post.id)
    }

    @Transactional
    @CacheEvict(cacheNames = ["post"], key = "#req.id")
    override fun edit(req: PostEditReq) {
        val post = findById(req.id!!)
        post.title = req.title
        post.content = req.content
        post.category = req.category
        post.keywords = req.keywords
        postRepository.save(post)
        log.info("update post with postId {}", post.id)
    }

    @Transactional
    @CacheEvict(cacheNames = ["post"], key = "#postId")
    override fun delete(postId: Long) {
        val post = findById(postId)
        postRepository.delete(post)
        log.info("post with id {} deleted", postId)
    }

    @Cacheable(cacheNames = ["post"], key = "'user:' + #userId + ':page:' + #page")
    override fun findUserPosts(userId: String, page: Int, size: Int): List<BlogPost> {
        return postRepository.findUserPosts(UUID.fromString(userId), PageRequest.of(page, size))
    }

    @Cacheable(cacheNames = ["post"], key = "#postId")
    override fun findById(postId: Long): BlogPost {
        return postRepository.findById(postId)
            .orElseThrow {
                log.warn(ErrorCode.POST_NOT_FOUND.message)
                BlogException(ErrorCode.POST_NOT_FOUND)
            }
    }

    override fun findAllPost(req: PostFindAllReq): List<BlogPost> {
        val startDate: Date = req.fromDate?.let { Date(it) } ?: Date(951900023099)
        val finishDate: Date = req.toDate?.let { Date(it) } ?: Date()
        val authorId: UUID? = req.authorId?.let { UUID.fromString(it) }
        return postRepository.findAllPosts(
            startDate,
            finishDate,
            authorId,
            req.category,
            req.keywords,
            PageRequest.of(req.page!!, req.size!!)
        ).orElse(mutableListOf())
    }

    fun accessLevelCheck(postId: Long, token: String): Boolean {
        if (!postRepository.existsById(postId)) {
            return true;
        }
        val userId = jwtService.getUuid(token)
        return postRepository.isUserValidToAccess(postId, UUID.fromString(userId))
    }
}
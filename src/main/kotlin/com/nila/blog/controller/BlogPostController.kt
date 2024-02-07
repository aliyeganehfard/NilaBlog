package com.nila.blog.controller

import com.nila.blog.common.config.JWTVerificationService
import com.nila.blog.common.dto.GeneralResponse
import com.nila.blog.common.dto.post.req.PostAddReq
import com.nila.blog.common.dto.post.req.PostEditReq
import com.nila.blog.common.dto.post.res.PostRes
import com.nila.blog.common.utils.Mapper
import com.nila.blog.database.model.BlogPost
import com.nila.blog.service.IBlogPostService
import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@RestController
@RequestMapping("v1/post/")
class BlogPostController {

    @Autowired
    lateinit var postService: IBlogPostService


    @Autowired
    lateinit var jwtService: JWTVerificationService

    val mapper = Mapper()

    @PostMapping("add")
    fun addPost(@RequestBody @Valid req: PostAddReq, @RequestHeader("Authorization") token: String)
            : ResponseEntity<GeneralResponse<Any>> {
        val userId = jwtService.getUuid(token)
        val post = mapper.toModel(req, BlogPost::class.java)
        postService.add(userId, post)
        val res = GeneralResponse.successfulResponse<Any>()
        return ResponseEntity(res, HttpStatus.OK)
    }

    @PutMapping("edit")
    @PreAuthorize("@blogPostService.accessLevelCheck(#req.id, #token)")
    fun editPost(@RequestBody @Valid req: PostEditReq, @RequestHeader("Authorization") token: String)
            : ResponseEntity<GeneralResponse<Any>> {
        postService.edit(req)
        val res = GeneralResponse.successfulResponse<Any>()
        return ResponseEntity(res, HttpStatus.OK)
    }

    @DeleteMapping("delete")
    @PreAuthorize("@blogPostService.accessLevelCheck(#postId, #token)")
    fun deletePost(@RequestParam(name = "postId") postId: Long, @RequestHeader("Authorization") token: String)
            : ResponseEntity<GeneralResponse<Any>> {
        postService.delete(postId)
        val res = GeneralResponse.successfulResponse<Any>()
        return ResponseEntity(res, HttpStatus.OK)
    }

    @GetMapping("user")
    fun findUserPosts(
        @NotNull(message = "page can not be null")
        @RequestParam(name = "page") page: Int,
        @NotNull(message = "size can not be null")
        @Min(value = 5, message = "size must between 5 to 50")
        @Max(value = 50, message = "size must between 5 to 50")
        @RequestParam(name = "size") size: Int,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<GeneralResponse<List<PostRes>>> {
        val userId = jwtService.getUuid(token)
        val posts = postService.findUserPosts(userId, page, size)
        val postsDto = mapper.toModelList(posts, PostRes::class.java)
        val res = GeneralResponse.successfulResponse(postsDto)
        return ResponseEntity(res, HttpStatus.OK)
    }

    @GetMapping("find/{post_id}")
    fun findPost(@PathVariable(name = "post_id") postId: Long): ResponseEntity<GeneralResponse<PostRes>> {
        val post = postService.findById(postId)
        val postDto = mapper.toDto(post, PostRes::class.java)
        val res = GeneralResponse.successfulResponse(postDto)
        return ResponseEntity(res, HttpStatus.OK)
    }
}
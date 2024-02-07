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
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
    fun editPost(@RequestBody @Valid req: PostEditReq): ResponseEntity<GeneralResponse<Any>> {
        postService.edit(req)
        val res = GeneralResponse.successfulResponse<Any>()
        return ResponseEntity(res, HttpStatus.OK)
    }

    @DeleteMapping("delete")
    fun deletePost(@RequestParam(name = "postId") postId: Long): ResponseEntity<GeneralResponse<Any>> {
        postService.delete(postId)
        val res = GeneralResponse.successfulResponse<Any>()
        return ResponseEntity(res, HttpStatus.OK)
    }

    @GetMapping("find/{post_id}")
    fun findPost(@PathVariable(name = "post_id") postId: Long): ResponseEntity<GeneralResponse<PostRes>> {
        val post = postService.findById(postId)
        val postDto = mapper.toDto(post, PostRes::class.java)
        val res = GeneralResponse.successfulResponse(postDto)
        return ResponseEntity(res, HttpStatus.OK)
    }

    @GetMapping("find/user")
    fun findUserPosts(
        @NotEmpty(message = "please enter userId")
        @RequestParam(name = "userId") userId: String,
        @NotNull(message = "page can not be null")
        @RequestParam(name = "page") page: Int,
        @NotNull(message = "size can not be null")
        @Min(value = 5, message = "size must between 5 to 50")
        @Max(value = 50, message = "size must between 5 to 50")
        @RequestParam(name = "size") size: Int
    ): ResponseEntity<GeneralResponse<List<PostRes>>> {
        val posts = postService.findUserPosts(userId, page, size)
        val postsDto = mapper.toModelList(posts, PostRes::class.java)
        val res = GeneralResponse.successfulResponse(postsDto)
        return ResponseEntity(res, HttpStatus.OK)
    }
}
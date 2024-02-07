package com.nila.blog.controller

import com.nila.blog.common.config.JWTVerificationService
import com.nila.blog.common.dto.GeneralResponse
import com.nila.blog.common.dto.comment.req.CommentAddReq
import com.nila.blog.common.dto.comment.req.CommentEditReq
import com.nila.blog.common.dto.comment.res.CommentRes
import com.nila.blog.common.utils.Mapper
import com.nila.blog.database.model.Comment
import com.nila.blog.service.ICommentService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@RestController
@RequestMapping("v1/comment/")
class CommentController {

    @Autowired
    lateinit var commentService: ICommentService

    val mapper = Mapper()

    @PostMapping("add")
    fun addComment(@RequestBody @Valid req: CommentAddReq)
            : ResponseEntity<GeneralResponse<Any>> {
        val comment = mapper.toModel(req, Comment::class.java)
        commentService.add(req.userId, comment)
        val res = GeneralResponse.successfulResponse<Any>()
        return ResponseEntity(res, HttpStatus.OK)
    }

    @PutMapping("edit")
    fun editComment(@RequestBody @Valid req: CommentEditReq): ResponseEntity<GeneralResponse<Any>> {
        commentService.edit(req)
        val res = GeneralResponse.successfulResponse<Any>()
        return ResponseEntity(res, HttpStatus.OK)
    }

    @DeleteMapping("delete")
    fun deleteComment(@RequestParam(name = "commentId") commentId: Long): ResponseEntity<GeneralResponse<Any>> {
        commentService.delete(commentId)
        val res = GeneralResponse.successfulResponse<Any>()
        return ResponseEntity(res, HttpStatus.OK)
    }

    @GetMapping("find/{comment_id}")
    fun findComment(@PathVariable(name = "comment_id") postId: Long): ResponseEntity<GeneralResponse<CommentRes>> {
        val post = commentService.findById(postId)
        val postDto = mapper.toDto(post, CommentRes::class.java)
        val res = GeneralResponse.successfulResponse(postDto)
        return ResponseEntity(res, HttpStatus.OK)
    }
}
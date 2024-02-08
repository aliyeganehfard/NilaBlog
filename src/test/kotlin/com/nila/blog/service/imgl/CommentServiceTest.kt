package com.nila.blog.service.imgl

import com.nila.blog.common.aop.exeptions.BlogException
import com.nila.blog.common.dto.comment.req.CommentEditReq
import com.nila.blog.database.model.BlogPost
import com.nila.blog.database.model.Comment
import com.nila.blog.database.model.User
import com.nila.blog.database.repository.CommentRepository
import com.nila.blog.service.IBlogPostService
import com.nila.blog.service.IUserService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
internal class CommentServiceTest{

    @Mock
    lateinit var commentRepository: CommentRepository

    @Mock
    lateinit var postService: IBlogPostService

    @Mock
    lateinit var userService: IUserService

    @InjectMocks
    private lateinit var commentService: CommentService

    @Test
    fun addComment() {
        val userId = UUID.randomUUID().toString()
        val post = BlogPost().apply { id = 1L }
        val comment = Comment().apply {
            id = 1L
            text = "comment text"
            this.post = post
        }

        val user = User().apply { username = "nila" }

        `when`(userService.findById(UUID.fromString(userId))).thenReturn(user)

        `when`(postService.findById(comment.post!!.id!!)).thenReturn(post)

        commentService.add(userId, comment)

        verify(commentRepository).save(comment)
    }

    @Test
    fun addWithInvalidUserId() {
        val userId = "440cd9c2-b215-405d-bc50-8e83ed5cffde"
        val comment = Comment().apply {
            id = 1L
            text = "This is a comment."
            post = BlogPost().apply { id = 1L }
        }

        `when`(userService.findById(UUID.fromString(userId))).thenThrow(BlogException::class.java)
        `when`(postService.findById(comment.post!!.id!!)).thenReturn(BlogPost())

        assertThrows(BlogException::class.java) {
            commentService.add(userId, comment)
        }
    }

    @Test
    fun addWithInvalidPostId() {
        val userId = "440cd9c2-b215-405d-bc50-8e83ed5cffde"
        val comment = Comment().apply {
            id = 1L
            text = "This is a comment."
            post = BlogPost().apply { id = 1L }
        }

        `when`(userService.findById(UUID.fromString(userId))).thenReturn(User())
        `when`(postService.findById(comment.post!!.id!!)).thenThrow(BlogException::class.java)

        assertThrows(BlogException::class.java) {
            commentService.add(userId, comment)
        }
    }

    @Test
    fun editComment() {
        val commentId = 1L
        val req = CommentEditReq().apply {
            id = commentId
            text = "Updated comment text"
        }

        val comment = Comment().apply { id = commentId }
        `when`(commentRepository.findById(commentId)).thenReturn(Optional.of(comment))

        commentService.edit(req)

        assertEquals(req.text, comment.text)
        verify(commentRepository).save(comment)
    }

    @Test
    fun editWithInvalidCommentId() {
        val commentId = -1L
        val req = CommentEditReq().apply {
            id = commentId
            text = "Updated comment text"
        }

        `when`(commentRepository.findById(commentId)).thenThrow(BlogException::class.java)

        assertThrows(BlogException::class.java) {
            commentService.edit(req)
        }
    }

    @Test
    fun deleteComment() {
        val commentId = 1L

        val comment = Comment().apply { id = commentId }
        `when`(commentRepository.findById(commentId)).thenReturn(Optional.of(comment))

        commentService.delete(commentId)

        verify(commentRepository).delete(comment)
    }

    @Test
    fun deleteWithInvalidCommentId() {
        val commentId = 100L

        `when`(commentRepository.findById(commentId)).thenThrow(BlogException::class.java)

        assertThrows(BlogException::class.java) {
            commentService.delete(commentId)
        }
    }

}
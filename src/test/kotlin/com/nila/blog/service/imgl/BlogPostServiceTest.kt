package com.nila.blog.service.imgl

import com.nila.blog.common.aop.exeptions.BlogException
import com.nila.blog.common.dto.post.req.PostEditReq
import com.nila.blog.database.model.BlogPost
import com.nila.blog.database.model.User
import com.nila.blog.database.model.enums.PostCategory
import com.nila.blog.database.repository.BlogPostRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
internal class BlogPostServiceTest{

    @Mock
    lateinit var postRepository: BlogPostRepository

    @Mock
    private lateinit var userService: UserService

    @InjectMocks
    private lateinit var postService: BlogPostService

    @Test
    fun addPost() {
        val userId = UUID.randomUUID().toString()
        val user = User().apply {
            id = UUID.fromString(userId)
            username = "user"
            email = "user@gmail.com"
            password = "password123"
        }

        val post = BlogPost().apply {
            id = 1L
            title = "Title"
            content = "This is a content of post."
            category = PostCategory.FINANCE
            keywords = listOf("FINANCE")
        }

        `when`(userService.findById(user.id!!)).thenReturn(user)

        postService.add(userId, post)

        verify(postRepository).save(post)
        assertEquals(user, post.author)
    }

    @Test
    fun addWithInvalidUserId() {
        val userId = UUID.randomUUID().toString()

        val post = BlogPost().apply {
            id = 1L
            title = "Title"
            content = "This is a content of post."
            category = PostCategory.FINANCE
            keywords = listOf("FINANCE")
        }

        `when`(userService.findById(UUID.fromString(userId))).thenThrow(BlogException::class.java)

        assertThrows(BlogException::class.java) {
            postService.add(userId, post)
        }
    }

    @Test
    fun editPost() {
        val postId = 1L
        val post = BlogPost().apply {
            id = postId
            title = "Old Title"
            content = "Old Content"
            category = PostCategory.FINANCE
            keywords = listOf("old")
        }

        val editReq = PostEditReq().apply {
            id = postId
            title = "New Title"
            content = "New Content"
            category = PostCategory.ENTERTAINMENT
            keywords = listOf("new")
        }

        `when`(postRepository.findById(postId)).thenReturn(Optional.of(post))

        postService.edit(editReq)

        verify(postRepository).save(post)
        assertEquals(editReq.title, post.title)
        assertEquals(editReq.content, post.content)
        assertEquals(editReq.category, post.category)
        assertEquals(editReq.keywords, post.keywords)
    }

    @Test
    fun editWithInvalidPostId() {
        val postId = 1L

        val postDto = PostEditReq().apply {
            id = 1L
            title = "Title"
            content = "This is a content of post."
            category = PostCategory.FINANCE
            keywords = listOf("FINANCE")
        }


        `when`(postRepository.findById(postId)).thenThrow(BlogException::class.java)

        assertThrows(BlogException::class.java) {
            postService.edit(postDto)
        }
    }

    @Test
    fun deletePost() {
        val postId = 1L
        val post = BlogPost().apply {
            id = postId
            title = "Post"
            content = "This is a content post."
            category = PostCategory.LIFESTYLE
            keywords = listOf("post")
        }

        `when`(postRepository.findById(postId)).thenReturn(Optional.of(post))

        postService.delete(postId)

        verify(postRepository).delete(post)
    }

    @Test
    fun deleteWithInvalidPostId() {
        val postId = 1L

        `when`(postRepository.findById(postId)).thenThrow(BlogException::class.java)

        assertThrows(BlogException::class.java) {
            postService.delete(postId)
        }
    }

}
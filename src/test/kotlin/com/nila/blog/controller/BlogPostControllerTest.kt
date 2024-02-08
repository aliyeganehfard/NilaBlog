package com.nila.blog.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.nila.blog.common.dto.post.req.PostAddReq
import com.nila.blog.common.dto.post.req.PostEditReq
import com.nila.blog.database.model.enums.PostCategory
import com.nila.blog.service.IAuthService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ComponentScan(basePackages = ["com.nila.blog"])
internal class BlogPostControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var authService: IAuthService

    lateinit var adminToken: String
    lateinit var token: String
    lateinit var userId: String

    @BeforeEach
    fun login() {
        val adminJwt = authService.singIn("admin", "admin123")
        val authorJwt = authService.singIn("author", "12345678")
        adminToken = adminJwt.accessToken!!
        token = authorJwt.accessToken!!
        userId = authorJwt.userId!!
    }

    @Test
    fun addPost() {
        val postDto = PostAddReq().apply {
            title = "Teaching"
            content = "Teaching is the process of imparting knowledge, skills, and values to students or learners"
            category = PostCategory.EDUCATION
            keywords = listOf("School", "Teacher")
        }

        mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/post/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postDto))
                .header("Authorization", "Bearer $token")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.rsCode").value(1000))
    }

    @Test
    fun editPost() {
        val postDto = PostEditReq().apply {
            id = 1
            title = "Fast Food"
            content = "Pizza Prepared and served rapidly"
            category = PostCategory.FOOD
            keywords = listOf("Pizza")
        }

        mockMvc.perform(
            MockMvcRequestBuilders.put("/v1/post/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postDto))
                .header("Authorization", "Bearer $token")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.rsCode").value(1000))
    }

    @Test
    fun editPostWithIncorrectId() {
        val postDto = PostEditReq().apply {
            id = 1000
            title = "Fast Food"
            content = "Pizza Prepared and served rapidly"
            category = PostCategory.FOOD
            keywords = listOf("Pizza")
        }

        mockMvc.perform(
            MockMvcRequestBuilders.put("/v1/post/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postDto))
                .header("Authorization", "Bearer $token")
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.rsCode").value(1065))
    }

    @Test
    fun editPostOfAnotherAuthor() {
        val postDto = PostEditReq().apply {
            id = 1
            title = "Fast Food"
            content = "Pizza Prepared and served rapidly"
            category = PostCategory.FOOD
            keywords = listOf("Pizza")
        }

        mockMvc.perform(
            MockMvcRequestBuilders.put("/v1/post/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postDto))
                .header("Authorization", "Bearer $adminToken")
        )
            .andExpect(MockMvcResultMatchers.status().isForbidden)
            .andExpect(MockMvcResultMatchers.jsonPath("$.rsCode").value(1002))
    }

    @Test
    fun deletePost() {
        val postId = 10
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/v1/post/delete?postId=$postId")
                .header("Authorization", "Bearer $token")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.rsCode").value(1000))
    }

    @Test
    fun deletePostWithIncorrect() {
        val incorrectPostId = 11
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/v1/post/delete?postId=$incorrectPostId")
                .header("Authorization", "Bearer $token")
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.rsCode").value(1065))
    }

    @Test
    fun deletePostOfAnotherAuthor() {
        val postId = 10
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/v1/post/delete?postId=$postId")
                .header("Authorization", "Bearer $adminToken")
        )
            .andExpect(MockMvcResultMatchers.status().isForbidden)
            .andExpect(MockMvcResultMatchers.jsonPath("$.rsCode").value(1002))
    }

    @Test
    fun findPostById() {
        val postId = 10
        mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/post/find/$postId")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.rsCode").value(1000))
    }

    @Test
    fun findPostByIdWithIncorrectId() {
        val incorrectId = 11
        mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/post/find/$incorrectId")
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.rsCode").value(1065))
    }

    @Test
    fun findAuthorPost() {
        val page = 0
        val size = 20
        mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/post/user?page=$page&size=$size")
                .header("Authorization", "Bearer $token")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.rsCode").value(1000))
    }

    @Test
    fun findAuthorPostWithIncorrectArgument() {
        val page = 0
        val size = 1
        mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/post/user?page=$page&size=$size")
                .header("Authorization", "Bearer $token")
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.rsCode").value(1100))
    }
}
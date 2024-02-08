package com.nila.blog.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.nila.blog.common.dto.BaseIdDto
import com.nila.blog.common.dto.comment.req.CommentAddReq
import com.nila.blog.common.dto.comment.req.CommentEditReq
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
internal class CommentControllerTest{

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var authService: IAuthService

    lateinit var commenterId: String

    @BeforeEach
    fun login() {
        val jwt = authService.singIn("admin", "admin123")
        commenterId = jwt.userId!!
    }

    @Test
    fun addComment() {
        val commentDto = CommentAddReq().apply {
            text = "Good!!!"
            post = BaseIdDto().apply {
                id = 10
            }
            userId = commenterId
        }

        mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/comment/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDto))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.rsCode").value(1000))
    }

    @Test
    fun addCommentWithIncorrectPost() {
        val commentDto = CommentAddReq().apply {
            text = "Good!!!"
            post = BaseIdDto().apply {
                id = 11
            }
            userId = commenterId
        }

        mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/comment/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDto))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.rsCode").value(1065))
    }

    @Test
    fun addCommentWithIncorrectUser() {
        val commentDto = CommentAddReq().apply {
            text = "Good!!!"
            post = BaseIdDto().apply {
                id = 10
            }
            userId = "440cd3c2-b215-475d-ec00-8e83ed5cf1dc"
        }

        mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/comment/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDto))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.rsCode").value(1050))
    }

    @Test
    fun editComment() {
        val commentDto = CommentEditReq().apply {
            id = 10
            text = "Very Good!!!"
        }

        mockMvc.perform(
            MockMvcRequestBuilders.put("/v1/comment/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDto))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.rsCode").value(1000))
    }

    @Test
    fun editCommentWithIncorrectId() {
        val commentDto = CommentEditReq().apply {
            id = 11
            text = "Very Good!!!"
        }

        mockMvc.perform(
            MockMvcRequestBuilders.put("/v1/comment/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDto))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.rsCode").value(1080))
    }

    @Test
    fun deleteComment() {
        val commentId = 10
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/v1/comment/delete?commentId=$commentId")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.rsCode").value(1000))
    }

    @Test
    fun deleteCommentWithIncorrectId() {
        val incorrectCommentId = 11
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/v1/comment/delete?commentId=$incorrectCommentId")
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.rsCode").value(1080))
    }

    @Test
    fun findCommentById() {
        val commentId = 10
        mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/comment/find/$commentId")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.rsCode").value(1000))
    }

    @Test
    fun findCommentByIncorrectId() {
        val incorrectCommentId = 11
        mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/comment/find/$incorrectCommentId")
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.rsCode").value(1080))
    }
}
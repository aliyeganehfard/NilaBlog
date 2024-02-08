package com.nila.blog.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.nila.blog.common.dto.user.req.UserEditProfileReq
import com.nila.blog.service.IAuthService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ComponentScan(basePackages = ["com.nila.blog"])
internal class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var authService: IAuthService

    lateinit var token: String
    lateinit var userId: String

    @Configuration
    class TestConfig {
        @Bean
        fun customMockMvcBuilder(context: WebApplicationContext): MockMvc {
            return MockMvcBuilders.webAppContextSetup(context).build()
        }

        @Bean
        fun objectMapper(): ObjectMapper {
            return ObjectMapper()
        }
    }

    @BeforeEach
    fun login() {
        val jwt = authService.singIn("admin", "admin123")
        token = jwt.accessToken!!
        userId = jwt.userId!!
    }

    @Test
    fun editUser() {
        val editDto = UserEditProfileReq().apply {
            username = "AdminPlus"
            email = "adminPlus@gmail.com"
        }

        mockMvc.perform(
            MockMvcRequestBuilders.put("/v1/user/profile/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(editDto))
                .header("Authorization", "Bearer $token")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.rsCode").value(1000))
    }

    @Test
    fun editUserWithInvalidArgument() {
        val editDto = UserEditProfileReq().apply {
            username = ""
            email = "adminPlus@gmail.com"
        }

        mockMvc.perform(
            MockMvcRequestBuilders.put("/v1/user/profile/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(editDto))
                .header("Authorization", "Bearer $token")
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.rsCode").value(1100))
    }

    @Test
    fun editUserWithDuplicateUsername() {
        val editDto = UserEditProfileReq().apply {
            username = "author"
            email = "adminPlus@gmail.com"
        }

        mockMvc.perform(
            MockMvcRequestBuilders.put("/v1/user/profile/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(editDto))
                .header("Authorization", "Bearer $token")
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.rsCode").value(1052))
    }

    @Test
    fun findById() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/user/profile/find?userId=$userId")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.rsCode").value(1000))
    }

    @Test
    fun findByIdWithIncorrectUserId() {
        val invalidUserId = "9785a57d-3bbd-450f-811a-611a718b8d32"
        mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/user/profile/find?userId=$invalidUserId")
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.rsCode").value(1050))
    }


}
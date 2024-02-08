package com.nila.blog.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.nila.blog.common.dto.authentication.req.SingUpReq
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.context.WebApplicationContext

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ComponentScan(basePackages = ["com.nila.blog"])
internal class AuthenticationControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

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


    @Test
    fun singUpUser() {
        val signUpReq = SingUpReq().apply {
            username = "nila"
            email = "nilaSoft@gmail.com"
            password = "12345678"
            confirmPassword = "12345678"
        }

        mockMvc.perform(
            post("/v1/auth/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpReq))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.rsCode").value(1000))
    }

    @Test
    fun singUpUserWithInvalidArgument() {
        val signUpReq = SingUpReq().apply {
            username = null
            email = null
            password = "12345678"
            confirmPassword = "12345678"
        }

        mockMvc.perform(
            post("/v1/auth/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpReq))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.rsCode").value(1100))
    }

    @Test
    fun singUpUserWithDifferentPasswordAndConfirmPassword() {
        val signUpReq = SingUpReq().apply {
            username = "soft"
            email = "soft@gmail.com"
            password = "12345678"
            confirmPassword = "87654321"
        }

        mockMvc.perform(
            post("/v1/auth/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpReq))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.rsCode").value(1051))
    }

    @Test
    fun singInUser() {
        val credential :MultiValueMap<String, String> = LinkedMultiValueMap()
        credential.add("username", "admin")
        credential.add("password", "admin123")

        mockMvc.perform(
            post("/v1/auth/signIn")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .params(credential)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.rsCode").value(1000))
    }

    @Test
    fun singInUserWithIncorrectUsername() {
        val credential :MultiValueMap<String, String> = LinkedMultiValueMap()
        credential.add("username", "nilasoft")
        credential.add("password", "12345678")

        mockMvc.perform(
            post("/v1/auth/signIn")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .params(credential)
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.rsCode").value(1050))
    }

    @Test
    fun singInUserWithIncorrectPassword() {
        val credential :MultiValueMap<String, String> = LinkedMultiValueMap()
        credential.add("username", "admin")
        credential.add("password", "87654321")

        mockMvc.perform(
            post("/v1/auth/signIn")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .params(credential)
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.rsCode").value(1053))
    }
}
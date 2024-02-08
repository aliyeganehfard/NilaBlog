package com.nila.blog.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

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
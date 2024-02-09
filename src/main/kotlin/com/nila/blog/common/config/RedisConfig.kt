package com.nila.blog.common.config

import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericToStringSerializer
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer

@Configuration
class RedisConfig {

    @Bean
    fun redisConnectionFactory(): LettuceConnectionFactory? {
        val properties = properties()
        val configuration = RedisStandaloneConfiguration(properties.host, properties.port)
        return LettuceConnectionFactory(configuration)
    }

    @Bean
    fun redisTemplate():RedisTemplate<String,Any>{
        val template = RedisTemplate<String,Any>()
        template.connectionFactory = redisConnectionFactory()
        template.keySerializer = JdkSerializationRedisSerializer()
        template.valueSerializer = GenericToStringSerializer(javaClass)
        return template
    }

    @Bean
    fun properties(): RedisProperties {
        return RedisProperties()
    }
}
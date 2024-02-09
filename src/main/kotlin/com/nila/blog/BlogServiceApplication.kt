package com.nila.blog

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cache.annotation.EnableCaching

@EnableCaching
@SpringBootApplication
class BlogServiceApplication

fun main(args: Array<String>) {
	SpringApplication.run(BlogServiceApplication::class.java, *args)
}

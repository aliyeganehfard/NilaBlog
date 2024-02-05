package com.nila.blog.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("info/v1/")
class InfoController {

    @GetMapping("time")
    fun currentTime(): String {
        return Date().toString()
    }
}
package com.nila.blog.common.dto.post.req

import jakarta.validation.constraints.NotEmpty
import java.io.Serializable

open class PostAddReq: Serializable {

    @NotEmpty(message = "please enter post title")
    lateinit var title: String

    @NotEmpty(message = "please enter post content")
    lateinit var content: String

}
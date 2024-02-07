package com.nila.blog.common.dto.post.req

import com.nila.blog.database.model.enums.PostCategory
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.io.Serializable

open class PostAddReq: Serializable {

    @NotEmpty(message = "please enter post title")
    lateinit var title: String

    @NotEmpty(message = "please enter post content")
    lateinit var content: String

    @NotNull(message = "select category for post")
    lateinit var category: PostCategory

    @NotNull(message = "please enter some post keywords")
    lateinit var keywords: List<String>
}
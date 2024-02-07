package com.nila.blog.common.dto.comment.req

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.io.Serializable

class CommentEditReq : Serializable {

    @Min(value = 1)
    @NotNull(message = "please provide comment id")
    var id: Long? = null

    @NotEmpty(message = "please enter comment text")
    lateinit var text: String

}
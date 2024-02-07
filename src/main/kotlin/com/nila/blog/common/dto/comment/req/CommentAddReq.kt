package com.nila.blog.common.dto.comment.req

import com.nila.blog.common.dto.BaseIdDto
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.io.Serializable

class CommentAddReq : Serializable {

    @NotEmpty(message = "please enter comment text")
    lateinit var text: String

    @Valid
    @NotNull(message = "please provide post id")
    lateinit var post: BaseIdDto

    @NotEmpty(message = "please enter user id")
    lateinit var userId: String
}
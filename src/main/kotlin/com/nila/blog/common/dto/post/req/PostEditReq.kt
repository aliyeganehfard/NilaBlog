package com.nila.blog.common.dto.post.req

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.io.Serializable

class PostEditReq : PostAddReq(), Serializable {

    @Min(value = 1)
    @NotNull(message = "please provide post id")
    var id: Long? = null
}
package com.nila.blog.common.dto.comment.res

import com.fasterxml.jackson.annotation.JsonInclude
import java.io.Serializable
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
class CommentRes: Serializable {
    var id: Long? = null
    var text: String? = null
    var createdAt: Date? = null
}
package com.nila.blog.common.dto.post.res

import com.fasterxml.jackson.annotation.JsonInclude
import java.io.Serializable
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
class PostRes : Serializable {

    var id: Long? = null
    var title: String? = null
    var content: String? = null
    var createdAt: Date? = null

}
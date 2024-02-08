package com.nila.blog.common.dto.post.res

import com.nila.blog.database.model.enums.PostCategory
import java.io.Serializable
import java.util.*

//@JsonInclude(JsonInclude.Include.NON_NULL)
class PostRes : Serializable {

    var id: Long? = null
    var title: String? = null
    var content: String? = null
    var category: PostCategory? = null
    var keywords: List<String>? = null
    var createdAt: Date? = null
    var author: UserIdRes? = null
}
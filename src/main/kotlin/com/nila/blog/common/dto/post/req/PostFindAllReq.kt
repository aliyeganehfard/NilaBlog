package com.nila.blog.common.dto.post.req

import com.nila.blog.database.model.enums.PostCategory
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.io.Serializable

class PostFindAllReq : Serializable {

    @NotNull(message = "page can not be null")
    var page: Int? = null

    @NotNull(message = "size can not be null")
    @Min(value = 5, message = "size must between 5 to 50")
    @Max(value = 50, message = "size must between 5 to 50")
    var size: Int? = null

    var fromDate: Long? = null
    var toDate: Long? = null
    var authorId : String? = null
    var category: PostCategory? = null
    var keywords: List<String>? = null
}
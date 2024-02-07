package com.nila.blog.common.dto.user.res

import com.fasterxml.jackson.annotation.JsonInclude
import java.io.Serializable
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
class UserProfileRes: Serializable {
    var id: UUID? = null
    var username: String? = null
    var email: String? = null
    var profilePictures: ByteArray? = null
}
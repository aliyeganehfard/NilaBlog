package com.nila.blog.common.dto.authentication.res

import com.fasterxml.jackson.annotation.JsonInclude
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AuthenticationResponse(
    var accessToken: String? = null,
    var tokenType: String? = null,
    var userId: String? = null,
) : Serializable

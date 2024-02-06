package com.nila.blog.common.dto.authentication

import java.io.Serializable

data class AuthenticationResponse(
    var accessToken: String? = null,
    var refreshToken: String? = null,
    var tokenType: String? = null,
    var userId: String? = null,
) : Serializable

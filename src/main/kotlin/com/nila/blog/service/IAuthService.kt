package com.nila.blog.service

import com.nila.blog.common.dto.authentication.res.AuthenticationResponse
import com.nila.blog.database.model.User

interface IAuthService {

    fun singUpUser(user: User, confirmPassword: String): AuthenticationResponse

    fun singIn(username: String, password: String): AuthenticationResponse
}
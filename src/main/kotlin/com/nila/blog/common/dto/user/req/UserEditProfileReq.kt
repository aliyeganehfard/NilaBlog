package com.nila.blog.common.dto.user.req

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import java.io.Serializable

class UserEditProfileReq: Serializable {

    @NotEmpty(message = "enter the username")
    lateinit var username: String

    @NotEmpty(message = "enter the email")
    @Email(message = "please provide a valid email address")
    lateinit var email: String
}
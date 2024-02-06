package com.nila.blog.common.dto.authentication.req

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern

class SingUpReq{
    @NotEmpty(message = "enter the username")
    var username: String? = null

    @Email(message = "please provide a valid email address")
    @NotEmpty(message = "enter the email")
    var email: String? = null

    @Pattern(regexp = "^.{8,}$", message = "password need more that 8 character")
    @NotEmpty(message = "enter the password")
    var password: String? = null

    @NotEmpty(message = "enter the confirmation password")
    var confirmPassword: String? = null
}

package com.nila.blog.common.dto.authentication.req

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern

class SingUpReq{
    @NotEmpty(message = "enter the username")
    var username: String? = null

    @NotEmpty(message = "enter the email")
    @Email(message = "please provide a valid email address")
    var email: String? = null

    @NotEmpty(message = "enter the password")
    @Pattern(regexp = "^.{8,}$", message = "password need more that 8 character")
    var password: String? = null

    @NotEmpty(message = "enter the confirmation password")
    var confirmPassword: String? = null
}

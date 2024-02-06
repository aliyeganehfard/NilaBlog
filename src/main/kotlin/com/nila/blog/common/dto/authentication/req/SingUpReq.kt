package com.nila.blog.common.dto.authentication.req

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern

data class SingUpReq(

    @NotEmpty(message = "username not entered!")
    var username: String? = null,

    @Email(message = "please provide a valid email address")
    @NotEmpty(message = "email address not entered!")
    var email: String? = null,

    @Pattern(regexp = "^.{8,}$", message = "password need more that 8 character")
    @NotEmpty(message = "password not entered!")
    var password: String? = null,

    @NotEmpty(message = "confirmation password not entered!")
    var confirmPassword: String? = null,

)

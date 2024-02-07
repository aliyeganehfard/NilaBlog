package com.nila.blog.common.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.io.Serializable

class BaseIdDto : Serializable {

    @Min(value = 1)
    @NotNull(message = "please provide id")
    var id: Long? = null

}
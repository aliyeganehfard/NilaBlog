package com.nila.blog.controller

import com.nila.blog.common.dto.GeneralResponse
import com.nila.blog.common.dto.authentication.req.SingUpReq
import com.nila.blog.common.dto.authentication.res.AuthenticationResponse
import com.nila.blog.common.utils.Mapper
import com.nila.blog.database.model.User
import com.nila.blog.service.IAuthService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping("v1/auth/")
class AuthenticationController {

    @Autowired
    lateinit var authService: IAuthService

    val mapper = Mapper()

    @PostMapping("singUp")
//    @PreAuthorize("permitAll()")
    fun singUpUser(@RequestBody @Valid req: SingUpReq): ResponseEntity<GeneralResponse<AuthenticationResponse>> {
        val user = mapper.toModel(req, User::class.java)
        val token = authService.singUpUser(user, req.confirmPassword!!)
        val res = GeneralResponse.successfulResponse(token)
        return ResponseEntity(res, HttpStatus.OK)
    }
}
package com.nila.blog.controller

import com.nila.blog.common.config.JWTVerificationService
import com.nila.blog.common.dto.GeneralResponse
import com.nila.blog.common.dto.user.req.UserEditProfileReq
import com.nila.blog.common.dto.user.res.UserProfileRes
import com.nila.blog.common.utils.Mapper
import com.nila.blog.database.model.User
import com.nila.blog.service.IUserService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
@RequestMapping("v1/user/profile/")
class UserController {

    @Autowired
    lateinit var userService: IUserService

    @Autowired
    lateinit var jwtService: JWTVerificationService

    val mapper = Mapper()

    @PutMapping("edit")
    fun editUserProfile(@RequestBody @Valid req: UserEditProfileReq,
                        @RequestHeader("Authorization") token: String): ResponseEntity<GeneralResponse<Any>>{
        val userId = jwtService.getUuid(token)
        userService.editProfile(userId, req)
        val res = GeneralResponse.successfulResponse<Any>()
        return ResponseEntity(res, HttpStatus.OK)
    }

    @PutMapping("image/upload")
    fun uploadUserProfile(
        @RequestParam("image") image: MultipartFile, @RequestHeader("Authorization") token: String
    ): ResponseEntity<GeneralResponse<Any>> {
        val userId = jwtService.getUuid(token)
        userService.uploadUserProfile(image, userId)
        val res = GeneralResponse.successfulResponse<Any>()
        return ResponseEntity(res, HttpStatus.OK)
    }

    @GetMapping("find")
    fun findProfilePicture(@RequestParam(name = "userId") userId: String): ResponseEntity<GeneralResponse<UserProfileRes>> {
        val user = userService.findById(UUID.fromString(userId))
        val profile = mapper.toDto(user, UserProfileRes::class.java)
        val res = GeneralResponse.successfulResponse(profile)
        return ResponseEntity(res, HttpStatus.OK)
    }
}
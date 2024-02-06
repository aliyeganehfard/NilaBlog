package com.nila.blog.common.aop

import com.auth0.jwt.exceptions.TokenExpiredException
import com.nila.blog.common.aop.exeptions.BlogException
import com.nila.blog.common.dto.GeneralResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(BlogException::class)
    fun blogException(blog: BlogException): ResponseEntity<GeneralResponse<Any>> {
        val res = GeneralResponse.unsuccessfulResponse<Any>(blog.errorCode!!)
        return ResponseEntity(res, blog.errorCode!!.httpStatus!!)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun accessDeniedException(accessDeniedException: AccessDeniedException): ResponseEntity<GeneralResponse<Any>> {
        val res = GeneralResponse.unsuccessfulResponse<Any>(ErrorCode.ACCESS_DENIED)
        return ResponseEntity(res, ErrorCode.ACCESS_DENIED.httpStatus!!)
    }

    @ExceptionHandler(TokenExpiredException::class)
    fun accessDeniedException(tokenExpiredException: TokenExpiredException): ResponseEntity<GeneralResponse<Any>> {
        val res = GeneralResponse.unsuccessfulResponse<Any>(ErrorCode.TOKEN_EXPIRED)
        return ResponseEntity(res, ErrorCode.TOKEN_EXPIRED.httpStatus!!)
    }

    @ExceptionHandler(Exception::class)
    fun accessDeniedException(exception: java.lang.Exception): ResponseEntity<GeneralResponse<Any>> {
        val res = GeneralResponse.unsuccessfulResponse<Any>(ErrorCode.INTERNAL_SERVER_ERROR)
        return ResponseEntity(res, ErrorCode.INTERNAL_SERVER_ERROR.httpStatus!!)
    }
}
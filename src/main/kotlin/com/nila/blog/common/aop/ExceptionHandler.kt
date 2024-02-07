package com.nila.blog.common.aop

import com.auth0.jwt.exceptions.TokenExpiredException
import com.nila.blog.common.aop.exeptions.BlogException
import com.nila.blog.common.dto.GeneralResponse
import jakarta.validation.ConstraintViolation
import jakarta.validation.ConstraintViolationException
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(BlogException::class)
    fun blogException(blog: BlogException): ResponseEntity<GeneralResponse<Any>> {
        val res = GeneralResponse.unsuccessfulResponse<Any>(blog.errorCode!!)
        return ResponseEntity(res, blog.errorCode!!.httpStatus!!)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun constraintViolationException(exception: ConstraintViolationException): ResponseEntity<GeneralResponse<Any>> {
        val violationArrayList: ArrayList<ConstraintViolation<*>> = ArrayList(exception.constraintViolations)
        val message = violationArrayList[0].messageTemplate
        val res = GeneralResponse.unsuccessfulResponse<Any>(ErrorCode.METHOD_ARGUMENT_NOT_VALID, message)
        return ResponseEntity(res, ErrorCode.METHOD_ARGUMENT_NOT_VALID.httpStatus!!)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidException(exception: MethodArgumentNotValidException): ResponseEntity<GeneralResponse<Any>> {
        val bindingResult: BindingResult = exception.bindingResult
        val message = bindingResult.fieldErrors
            .stream()
            .findFirst()
            .map { obj: FieldError -> obj.defaultMessage }
            .orElse(ErrorCode.METHOD_ARGUMENT_NOT_VALID.message)
        val res = GeneralResponse.unsuccessfulResponse<Any>(ErrorCode.METHOD_ARGUMENT_NOT_VALID, message!!)
        return ResponseEntity(res, ErrorCode.METHOD_ARGUMENT_NOT_VALID.httpStatus!!)
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

    @ExceptionHandler(InternalAuthenticationServiceException::class)
    fun internalAuthenticationServiceException(exception: InternalAuthenticationServiceException)
            : ResponseEntity<GeneralResponse<Any>> {
        val res = GeneralResponse.unsuccessfulResponse<Any>(ErrorCode.USER_NOT_FOUND, exception.message!!)
        return ResponseEntity(res, ErrorCode.USER_NOT_FOUND.httpStatus!!)
    }

    @ExceptionHandler(Exception::class)
    fun exception(exception: Exception): ResponseEntity<GeneralResponse<Any>> {
        val res = GeneralResponse.unsuccessfulResponse<Any>(ErrorCode.INTERNAL_SERVER_ERROR)
        return ResponseEntity(res, ErrorCode.INTERNAL_SERVER_ERROR.httpStatus!!)
    }
}